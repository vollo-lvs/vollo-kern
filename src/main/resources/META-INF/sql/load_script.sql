insert into scholen (id, version, naam, hoort_bij_school_id) values (1, 0, 'osg Hoofdschool', null);
insert into scholen (id, version, naam, hoort_bij_school_id) values (2, 0, 'osg Subschool', 1);
insert into scholen (id, version, naam, hoort_bij_school_id) values (3, 0, 'osg Onderschool', 1);

insert into groepen (id, version, naam, school_id) values (nextval('vollo_seq'), 0, 'Groep 1', 2);
insert into groepen (id, version, naam, school_id) values (nextval('vollo_seq'), 0, 'Groep 2', 2);
insert into groepen (id, version, naam, school_id) values (nextval('vollo_seq'), 0, 'Groep 3', 2);
insert into groepen (id, version, naam, school_id) values (nextval('vollo_seq'), 0, 'Groep 4', 2);
insert into groepen (id, version, naam, school_id) values (nextval('vollo_seq'), 0, 'Groep 5', 2);
insert into groepen (id, version, naam, school_id) values (nextval('vollo_seq'), 0, 'Groep 6', 2);
insert into groepen (id, version, naam, school_id) values (nextval('vollo_seq'), 0, 'Groep 7', 2);
insert into groepen (id, version, naam, school_id) values (nextval('vollo_seq'), 0, 'Groep 8', 2);

insert into leerlingen (id, version, voornamen, roepnaam, achternaam, geboortedatum) values (nextval('vollo_seq'), 0, 'a', 'a', 'a', now());
insert into leerlingen (id, version, voornamen, roepnaam, achternaam, geboortedatum) values (nextval('vollo_seq'), 0, 'b', 'b', 'b', now());
insert into leerlingen (id, version, voornamen, roepnaam, achternaam, geboortedatum) values (nextval('vollo_seq'), 0, 'c', 'c', 'c', now());

insert into groep_leerlingen (id, version, datum_begin, groep_id, leerling_id) select nextval('vollo_seq'), 0, now(), grp.id, llg.id from groepen grp, leerlingen llg;

insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'k', 'k');
insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'l', 'l');
insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'm', 'm');

insert into groep_medewerkers (id, version, datum_begin, groep_id, medewerker_id) select nextval('vollo_seq'), 0, now(), grp.id, mdw.id from groepen grp, medewerkers mdw;
