CREATE TABLE newsletter (
  id integer PRIMARY KEY NOT NULL,
  issue text NOT NULL,
  issue_date DATE NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_synched TIMESTAMP NOT NULL DEFAULT current_timestamp,

  CONSTRAINT idx_issue UNIQUE (issue)
);

-- this table corresponds to newsletter_topic_tr in core.
-- hence id is not unique here (and thus only part of the compound primary key together with lang_code)
CREATE TABLE newsletter_topic (
  id integer NOT NULL,
  lang_code text NOT NULL,
  title text NOT NULL,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_synched TIMESTAMP NOT NULL DEFAULT current_timestamp,

   PRIMARY KEY (id, lang_code)
);


CREATE TABLE new_study_topic (
  newsletter_id integer NOT NULL,
  newsletter_topic_id integer NOT NULL,
  sort integer NOT NULL DEFAULT 0,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_synched TIMESTAMP NOT NULL DEFAULT current_timestamp,

  PRIMARY KEY (newsletter_id, newsletter_topic_id),
  FOREIGN KEY (newsletter_id) REFERENCES newsletter(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- paper_number is unique in table but not PK. We need number instead id. avoiding join but unable to use FK.
CREATE TABLE new_study (
  newsletter_id integer NOT NULL,
  paper_number bigint NOT NULL,
  newsletter_topic_id int,
  sort integer NOT NULL DEFAULT 0,
  year integer NOT NULL,
  authors text NOT NULL,
  headline text,
  description text,

  version integer DEFAULT 1,
  created TIMESTAMP DEFAULT current_timestamp,
  last_modified TIMESTAMP DEFAULT current_timestamp,
  last_synched TIMESTAMP NOT NULL DEFAULT current_timestamp,

  PRIMARY KEY (newsletter_id, paper_number),
  FOREIGN KEY (newsletter_id, newsletter_topic_id) REFERENCES new_study_topic(newsletter_id, newsletter_topic_id)
);
