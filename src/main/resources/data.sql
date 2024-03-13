insert into user_accounts(name, password, roles) values ('admin', '$2a$10$mW8sPRhbOxSuXCWXh./H5.dLCzQ35CjBSoAg/P3.LgfsrU.493vJO', 'ROLE_ADMIN')
                                            on conflict do nothing;
