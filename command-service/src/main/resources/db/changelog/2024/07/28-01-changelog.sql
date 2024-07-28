-- liquibase formatted sql

-- changeset Torchez:1722187898935-1
ALTER TABLE simple_entity
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE simple_entity
    ADD updated_at TIMESTAMP WITHOUT TIME ZONE;
ALTER TABLE simple_entity
    ADD version INTEGER;

-- changeset Torchez:1722187898935-4
ALTER TABLE simple_entity
    ALTER COLUMN version SET NOT NULL;

