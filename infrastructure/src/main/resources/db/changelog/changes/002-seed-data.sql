--liquibase formatted sql

--changeset cfa:002-seed-data context:seed
INSERT INTO candidates (id, email, first_name, last_name, selected_program, eligibility_status, created_at, updated_at)
VALUES
    ('11111111-1111-1111-1111-111111111101', 'alice@example.com', 'Alice', 'Anderson', 'LEVEL_I', 'PENDING', NOW(), NOW()),
    ('11111111-1111-1111-1111-111111111102', 'bob@example.com', 'Bob', 'Baker', 'LEVEL_II', 'ELIGIBLE', NOW(), NOW()),
    ('11111111-1111-1111-1111-111111111103', 'carol@example.com', 'Carol', 'Clark', 'LEVEL_III', 'PENDING', NOW(), NOW());

INSERT INTO education (id, candidate_id, degree_level, institution, graduation_year)
VALUES
    ('22222222-2222-2222-2222-222222222201', '11111111-1111-1111-1111-111111111101', 'BACHELORS', 'State University', 2018);

INSERT INTO professional_experience (id, candidate_id, employer, role, start_date, end_date)
VALUES
    ('33333333-3333-3333-3333-333333333301', '11111111-1111-1111-1111-111111111103', 'Finance Corp', 'Analyst', '2016-01-01', '2020-12-31');

INSERT INTO exam_passes (id, candidate_id, level, passed_at)
VALUES
    ('44444444-4444-4444-4444-444444444401', '11111111-1111-1111-1111-111111111102', 'LEVEL_I', '2022-06-15'),
    ('44444444-4444-4444-4444-444444444402', '11111111-1111-1111-1111-111111111103', 'LEVEL_I', '2021-08-01'),
    ('44444444-4444-4444-4444-444444444403', '11111111-1111-1111-1111-111111111103', 'LEVEL_II', '2023-11-20');

--rollback DELETE FROM exam_passes WHERE id LIKE '44444444%'; DELETE FROM professional_experience WHERE id LIKE '33333333%'; DELETE FROM education WHERE id LIKE '22222222%'; DELETE FROM candidates WHERE id LIKE '11111111%';
