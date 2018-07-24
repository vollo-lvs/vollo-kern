insert into scholen (id, version, naam, hoort_bij_school_id) values (1, 0, 'osg Hoofdschool', null);
insert into scholen (id, version, naam, hoort_bij_school_id) values (2, 0, 'osg Subschool', 1);
insert into scholen (id, version, naam, hoort_bij_school_id) values (3, 0, 'osg Onderschool', 1);

insert into groepen (id, version, naam, school_id, niveau) values (nextval('vollo_seq'), 0, 'Groep 1', 2, 1);
insert into groepen (id, version, naam, school_id, niveau) values (nextval('vollo_seq'), 0, 'Groep 2', 2, 2);
insert into groepen (id, version, naam, school_id, niveau) values (nextval('vollo_seq'), 0, 'Groep 3', 2, 3);
insert into groepen (id, version, naam, school_id, niveau) values (nextval('vollo_seq'), 0, 'Groep 4', 2, 4);
insert into groepen (id, version, naam, school_id, niveau) values (nextval('vollo_seq'), 0, 'Groep 5', 2, 5);
insert into groepen (id, version, naam, school_id, niveau) values (nextval('vollo_seq'), 0, 'Groep 6', 2, 6);
insert into groepen (id, version, naam, school_id, niveau) values (nextval('vollo_seq'), 0, 'Groep 7', 2, 7);
insert into groepen (id, version, naam, school_id, niveau) values (nextval('vollo_seq'), 0, 'Groep 8', 2, 8);

insert into leerlingen (id, version, voornamen, roepnaam, achternaam, geboortedatum, geslacht) select nextval('vollo_seq'), 0, chr(64 + generate_series) || repeat(chr(96 ++ generate_series), 4), chr(64 + generate_series) || repeat(chr(96 + generate_series), 4), 'Leerling' || chr(96 + generate_series) || niveau, now(), case mod(generate_series, 3) when 0 then 'MAN' when 1 then 'VROUW' else 'OVERIG' end from generate_series(1,26), groepen;

insert into groep_leerlingen (id, version, datum_begin, groep_id, leerling_id) select nextval('vollo_seq'), 0, now(), grp.id, llg.id from groepen grp, leerlingen llg where to_number(right(llg.achternaam, 1), '9') = grp.niveau;

insert into inschrijvingen (id, version, datum_inschrijving, leerling_id, school_id) select nextval('vollo_seq'), 0, '2017-09-01', id, 2 from leerlingen;

insert into toetsen (id, version, datum, soort, soort_score, omschrijving) values (nextval('vollo_seq'), 0, now(), 'Dictee', 'CIJFER_1_10', 'Eindejaars dictee');

insert into scores (id, version, leerling_id, toets_id, cijfer_score) select nextval('vollo_seq'), 0, llg.id, tts.id, random() * 10 from leerlingen llg, toetsen tts where right(achternaam, 1) = '3';

insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'k', 'k');
insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'l', 'l');
insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'm', 'm');

insert into groep_medewerkers (id, version, datum_begin, groep_id, medewerker_id) select nextval('vollo_seq'), 0, now(), grp.id, mdw.id from groepen grp, medewerkers mdw;

insert into gebruikers (id, gebruikersnaam, version, wachtwoord, medewerker_id, ouder_id, rollen) select nextval('vollo_seq'), achternaam, 0, achternaam, id, null, 'ROLE_GEBRUIKER' from medewerkers;
