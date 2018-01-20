insert into groepen (id, version, naam) values (nextval('vollo_seq'), 0, 'Groep 1');
insert into groepen (id, version, naam) values (nextval('vollo_seq'), 0, 'Groep 2');
insert into groepen (id, version, naam) values (nextval('vollo_seq'), 0, 'Groep 3');
insert into groepen (id, version, naam) values (nextval('vollo_seq'), 0, 'Groep 4');
insert into groepen (id, version, naam) values (nextval('vollo_seq'), 0, 'Groep 5');
insert into groepen (id, version, naam) values (nextval('vollo_seq'), 0, 'Groep 6');
insert into groepen (id, version, naam) values (nextval('vollo_seq'), 0, 'Groep 7');
insert into groepen (id, version, naam) values (nextval('vollo_seq'), 0, 'Groep 8');

insert into leerlingen (id, version, voornamen, roepnaam, achternaam, geboortedatum) values (nextval('vollo_seq'), 0, 'a', 'a', 'a', now());
insert into leerlingen (id, version, voornamen, roepnaam, achternaam, geboortedatum) values (nextval('vollo_seq'), 0, 'b', 'b', 'b', now());
insert into leerlingen (id, version, voornamen, roepnaam, achternaam, geboortedatum) values (nextval('vollo_seq'), 0, 'c', 'c', 'c', now());

insert into groep_leerlingen (id, version, datum_begin, groep_id, leerling_id) select nextval('vollo_seq'), 0, now(), grp.id, llg.id from groepen grp, leerlingen llg;

insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'k', 'k');
insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'l', 'l');
insert into medewerkers (id, version, voornaam, achternaam) values (nextval('vollo_seq'), 0, 'm', 'm');

insert into groep_medewerkers (id, version, datum_begin, groep_id, medewerker_id) select nextval('vollo_seq'), 0, now(), grp.id, mdw.id from groepen grp, medewerkers mdw;
