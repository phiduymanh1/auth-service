CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================================================================
-- INIT ENUM TYPE
-- =========================================================================
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_status') THEN
CREATE TYPE user_status AS ENUM ('ACTIVE', 'INACTIVE', 'LOCKED', 'BANNED');
END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'token_type') THEN
CREATE TYPE token_type AS ENUM ('EMAIL_VERIFICATION', 'PASSWORD_RESET', 'MFA_VERIFICATION');
END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'login_status_type') THEN
CREATE TYPE login_status_type AS ENUM ('SUCCESS', 'FAILED', 'MFA_REQUIRED');
END IF;
END $$;

-- =========================================================================
-- 1. TABLE APPLICATIONS
-- =========================================================================
CREATE TABLE IF NOT EXISTS applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    client_id VARCHAR(100) NOT NULL UNIQUE,  -- Cung cấp cho backend ứng dụng vệ tinh định danh
    client_secret VARCHAR(255) NOT NULL,    -- Chuỗi bảo mật giao tiếp nội bộ giữa các dịch vụ
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- =========================================================================
-- 2. ACCOUNT CORE AND PERMISSION MANAGEMENT TABLES
-- =========================================================================

-- USERS (Global Identity Account)
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NULL,        -- Allow NULL to support the Google/Facebook Login (OAuth2) flow.
    status user_status NOT NULL DEFAULT 'ACTIVE',
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    locked_until TIMESTAMPTZ NULL,
    last_login_at TIMESTAMPTZ NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- ROLES (System role groups - Global)
CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,       -- VD: 'ROLE_ADMIN', 'ROLE_STUDENT'
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- PERMISSIONS (Detailed Action Rights - Global)
CREATE TABLE IF NOT EXISTS permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,      -- VD: 'course:create', 'task:delete'
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- USER ROLES (Three-way relationship: A user has a specific role on a specific application.)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    application_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY(user_id, role_id, application_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_app FOREIGN KEY(application_id) REFERENCES applications(id) ON DELETE CASCADE
    );

-- ROLE PERMISSIONS (Quan hệ Nhiều - Nhiều giữa Nhóm vai trò và Quyền chi tiết)
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY(role_id, permission_id),
    CONSTRAINT fk_role_permissions_role FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY(permission_id) REFERENCES permissions(id) ON DELETE CASCADE
    );

-- =========================================================================
-- 3. CÁC BẢNG TIỆN ÍCH BẢO MẬT, ĐĂNG NHẬP VÀ GIÁM SÁT
-- =========================================================================

-- SECURITY TOKENS (Quản lý tập trung Token Xác thực Email / Reset Password cho từng App)
CREATE TABLE IF NOT EXISTS security_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    application_id UUID NOT NULL,           -- Xác định token được phát sinh từ ứng dụng nào
    token_hash VARCHAR(255) NOT NULL,
    type token_type NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_security_tokens_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_security_tokens_app FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE
    );

-- LOGIN HISTORIES (Lịch sử đăng nhập theo từng Ứng dụng cụ thể)
CREATE TABLE IF NOT EXISTS login_histories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NULL,
    application_id UUID NULL,               -- Biết chính xác user vừa đăng nhập vào App nào
    email VARCHAR(255) NOT NULL,
    ip_address VARCHAR(100),
    user_agent TEXT,
    login_status login_status_type NOT NULL,
    failure_reason VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_login_history_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_login_history_app FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE SET NULL
    );

-- MFA SETTINGS (Cấu hình mã OTP bảo mật 2 lớp gắn với định danh cá nhân của User)
CREATE TABLE IF NOT EXISTS mfa_settings (
    user_id UUID PRIMARY KEY,
    secret_key VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    backup_codes VARCHAR(255)[] NULL,       -- Mảng chứa các mã khôi phục dự phòng chống mất thiết bị
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_mfa_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- OAUTH ACCOUNTS (Liên kết tài khoản Google / Facebook với tài khoản hệ thống)
CREATE TABLE IF NOT EXISTS oauth_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    provider VARCHAR(50) NOT NULL,          -- VD: 'GOOGLE', 'FACEBOOK'
    provider_user_id VARCHAR(255) NOT NULL, -- ID định danh duy nhất của bên thứ ba cấp
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_provider_user UNIQUE(provider, provider_user_id),
    CONSTRAINT fk_oauth_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
    );

-- AUDIT LOGS (Nhật ký giám sát biến động dữ liệu nhạy cảm theo từng App)
CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NULL,
    application_id UUID NULL,               -- Xác định hành động chỉnh sửa dữ liệu này thuộc App nào
    action VARCHAR(100) NOT NULL,           -- VD: 'PASSWORD_CHANGED', 'USER_BANNED'
    entity_name VARCHAR(100),
    entity_id VARCHAR(255),
    old_value JSONB,
    new_value JSONB,
    ip_address VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(), -- Đã sửa lỗi thành TIMESTAMPTZ để đồng bộ múi giờ

    CONSTRAINT fk_audit_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_audit_app FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE SET NULL
    );

-- =========================================================================
-- 4. TỰ ĐỘNG HÓA TẦNG DATABASE (TRIGGERS CẬP NHẬT UPDATED_AT)
-- =========================================================================
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger bảng applications
DROP TRIGGER IF EXISTS set_timestamp_applications ON applications;
CREATE TRIGGER set_timestamp_applications BEFORE UPDATE ON applications FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

-- Trigger bảng users
DROP TRIGGER IF EXISTS set_timestamp_users ON users;
CREATE TRIGGER set_timestamp_users BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

-- Trigger bảng roles
DROP TRIGGER IF EXISTS set_timestamp_roles ON roles;
CREATE TRIGGER set_timestamp_roles BEFORE UPDATE ON roles FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

-- Trigger bảng mfa_settings
DROP TRIGGER IF EXISTS set_timestamp_mfa ON mfa_settings;
CREATE TRIGGER set_timestamp_mfa BEFORE UPDATE ON mfa_settings FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();


CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles(role_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_app_id ON user_roles(application_id); -- Quét nhanh quyền của User thuộc App cụ thể

CREATE INDEX IF NOT EXISTS idx_role_permissions_permission_id ON role_permissions(permission_id);

-- Index gộp (Composite Index) tối ưu hóa việc tra cứu và sắp xếp lịch sử
CREATE INDEX IF NOT EXISTS idx_login_histories_user_app ON login_histories(user_id, application_id);
CREATE INDEX IF NOT EXISTS idx_login_histories_created_at ON login_histories(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_audit_logs_user_app ON audit_logs(user_id, application_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_created_at ON audit_logs(created_at DESC);

-- Tối ưu hóa tuyệt đối tốc độ tìm kiếm mã băm Token từ Email link gửi lên Backend
CREATE UNIQUE INDEX IF NOT EXISTS idx_security_tokens_hash ON security_tokens(token_hash);
CREATE INDEX IF NOT EXISTS idx_security_tokens_user_id ON security_tokens(user_id);

CREATE INDEX IF NOT EXISTS idx_oauth_accounts_provider_uid ON oauth_accounts(provider, provider_user_id);