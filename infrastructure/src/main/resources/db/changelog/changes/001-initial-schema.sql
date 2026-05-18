--liquibase formatted sql

--changeset cfa:001-initial-schema
CREATE TABLE candidates (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    selected_program VARCHAR(20) NOT NULL,
    eligibility_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX uk_candidates_email_active ON candidates (email) WHERE deleted_at IS NULL;

CREATE TABLE education (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES candidates (id),
    degree_level VARCHAR(20) NOT NULL,
    institution VARCHAR(255) NOT NULL,
    graduation_year INTEGER
);

CREATE TABLE professional_experience (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES candidates (id),
    employer VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE
);

CREATE TABLE exam_passes (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES candidates (id),
    level VARCHAR(20) NOT NULL,
    passed_at DATE NOT NULL
);

CREATE TABLE eligibility_audits (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL REFERENCES candidates (id),
    verified_by VARCHAR(255) NOT NULL,
    verified_at TIMESTAMPTZ NOT NULL,
    decision VARCHAR(20) NOT NULL,
    reason TEXT NOT NULL
);

CREATE INDEX idx_candidates_status_program ON candidates (eligibility_status, selected_program)
    WHERE deleted_at IS NULL;

--rollback DROP TABLE IF EXISTS eligibility_audits, exam_passes, professional_experience, education, candidates CASCADE;
