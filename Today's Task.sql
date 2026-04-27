use mydb;
create table employees (id int , name text, department text, salary float, joining_date date);
insert into employees values
(1, 'Amit', 'Sales', 60000, '2023-01-10'),
(2, 'Neha', 'HR', 45000, '2022-03-15'),
(3, 'Raj', 'Sales', 70000, '2021-07-20'),
(4, 'Simran', 'IT', 80000, '2020-11-05'),
(5, 'Karan', 'IT', 55000, '2022-08-18'),
(6, 'Pooja', 'HR', 48000, '2023-02-25'),
(7, 'Ravi', 'Sales', 52000, '2021-12-12'),
(8, 'Anjali', 'IT', 90000, '2019-09-30');
create table depts(dept_id int primary key, dept_name text);
insert into depts values
(1, 'Sales'),
(2, 'HR'),
(3, 'IT'),
(4, 'Marketing');
create table employees2 (emp_id int primary key, name text,dept_id int, foreign key (dept_id) references depts(dept_id));
insert into employees2 values
(101, 'Amit', 1),
(102, 'Neha', 2),
(103, 'Raj', 1),
(104, 'Simran', 3),
(105, 'Karan', 3),
(106, 'Pooja', 2),
(107, 'Ravi', NULL),   -- no department
(108, 'Anjali', 3);
create table products (id int, name text, price float, stock int);
insert into products values
(1, 'Laptop', 60000, 10),
(2, 'Phone', 30000, 20),
(3, 'Tablet', 20000, 0),
(4, 'Headphones', 2000, 15),
(5, 'Keyboard', 1000, 0);
create table customers (id int primary key,name text);
insert into customers values
(1, 'Amit'),
(2, 'Neha'),
(3, 'Raj'),
(4, 'Simran'),
(5, 'Karan');
DROP TABLE IF EXISTS customers;
create table orders(id int primary key, customer_id int, amount float, order_date date, foreign key (customer_id) references customers(id));
insert into orders values
(1, 1, 5000, '2024-01-10'),
(2, 1, 3000, '2024-02-15'),
(3, 2, 7000, '2024-01-20'),
(4, 3, 2000, '2024-03-05'),
(5, 3, 4000, '2024-03-18'),
(6, 4, 10000, '2024-02-25');
-- Basic Queries
select * from employees where department = 'Sales';
select * from employees where salary > 50000;
select * from employees order by salary desc;
select name, joining_date from employees;
-- Filtering & Aggregation
select department, count(*) as total_employees from employees group by department;
select department, avg(salary) as avg_salary from employees group by department;
select department, max(salary) as highest_salary from employees group by department;
select department, count(*) as total_employees from employees group by department having count(*) > 5;
-- Joins
select e.name, d.dept_name from employees2 e Join depts d on e.dept_id = d.dept_id;
select e.name from employees2 e left Join depts d on e.dept_id = d.dept_id where d.dept_id is null;
select d.dept_name, e.name from depts d left Join employees2 e on d.dept_id = e.dept_id;
-- Subqueries
select * from employees where salary > (select avg(salary) from employees);
select max(salary) from employees where salary < (select max(salary) from employees);
select * from employees e where salary = (select max(salary) from employees where department = e.department);
-- Data Manipulation
insert into products (id, name, price, stock) values
(1, 'Laptop', 60000, 10),
(2, 'Phone', 30000, 20),
(3, 'Tablet', 20000, 0),
(4, 'Headphones', 2000, 15),
(5, 'Keyboard', 1000, 0);
update products set price = 65000 where id = 1;
delete from products where stock = 0;
update products set price = price * 1.10;
-- Real-World Scenario
select c.name, sum(o.amount) as total_spent from customers c Join orders o on c.id = o.customer_id group by c.name;
select c.name, sum(o.amount) as total_spent from customers c Join orders o on c.id = o.customer_id group by c.name
order by total_spent desc limit 3;
select c.name from customers c left Join orders o on c.id = o.customer_id where o.id is null;
select month(order_date) as month, sum(amount) as revenue from orders group by month(order_date);
-- Advanced
delete e1 from employees e1 Join employees e2 on e1.name = e2.name and e1.department = e2.department and e1.id > e2.id;
create view top_customers as select c.name, sum(o.amount) as total_spent from customers c Join orders o 
on c.id = o.customer_id group by c.name order by total_spent desc;
select name, department, salary, rank() over (order by salary desc) as rank_salary from employees;
select order_date, sum(amount) over (order by order_date) as running_total from orders;
