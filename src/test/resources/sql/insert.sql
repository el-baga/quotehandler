insert into quote (id, isin, bid, ask)
values (1, 'AGHTNBKGHVRT', 100.5, 100.7),
       (2, 'UUURNB5GHV34', 101.8, 101.9);

SELECT setval ('quote_id_seq', (SELECT MAX(id) FROM quote));

insert into energy_lvl (id, best_price, quote_id)
values (1, 100.5, 1),
       (2, 101.8, 2);

SELECT setval ('energy_lvl_id_seq', (SELECT MAX(id) FROM energy_lvl));