insert into quote (id, isin, bid, ask)
values (1, 'AGHTNBKGHVRT', 100.5, 100.7);

SELECT setval ('quote_id_seq', (SELECT MAX(id) FROM quote));

insert into energy_lvl (id, best_price, quote_id)
values (1, 100.5, 1);

SELECT setval ('energy_lvl_id_seq', (SELECT MAX(id) FROM energy_lvl));