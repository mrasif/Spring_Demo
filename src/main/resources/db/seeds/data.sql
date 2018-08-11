INSERT INTO roles(`role_id`,`role`) VALUES(1,'ADMIN');
INSERT INTO roles(`role_id`,`role`) VALUES(2,'MODERATOR');
INSERT INTO roles(`role_id`,`role`) VALUES(3,'USER');
INSERT INTO `users`(`user_id`, `active`, `email`, `first_name`, `last_name`, `password`, `username`) VALUES(1, 1, 'admin@mrasif.in', 'Admin', 'User', '12345678', 'admin');
INSERT INTO `user_role`(`user_id`, `role_id`) VALUES(1,	1);