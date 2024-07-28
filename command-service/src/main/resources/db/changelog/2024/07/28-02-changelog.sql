-- liquibase formatted sql

-- changeset Torchez:1722189305626-1
ALTER TABLE simple_entity
    ALTER COLUMN email DROP NOT NULL;

-- changeset Torchez:1722189305626-2
ALTER TABLE simple_entity
    ALTER COLUMN full_name DROP NOT NULL;

-- changeset Torchez:1722189305626-3
ALTER TABLE simple_entity
    ALTER COLUMN version SET NOT NULL;

