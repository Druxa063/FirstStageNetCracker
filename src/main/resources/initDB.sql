DROP TABLE IF EXISTS hero;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 100000;

CREATE TABLE hero
(
  id INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name          VARCHAR(30)      NOT NULL,
  universe      VARCHAR(100)      NOT NULL,
  power         INTEGER           NOT NULL,
  description   VARCHAR(255)      NOT NULL,
  alive         BOOLEAN           NOT NULL
);
CREATE UNIQUE INDEX hero_unique_name ON hero (name);

INSERT INTO hero (name, universe, power, description, alive) VALUES
  ('Iron man', 'marvel', 60, 'гений, миллиардер, плейбой, филантроп', TRUE),
  ('Spider-man', 'marvel', 50, 'Школьник укушенный пауком', FALSE);