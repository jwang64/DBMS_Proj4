# Database: Project 4

Objective: Set up mysql and postgres, run some queries, make them faster, and plot the difference.

## Group Members
| Role       | Name            | Student ID |
| ---------- | --------------- | ---------- |
| Manager    | James Wang      | 811015135  |
| Programmer | Anurag Banerjee | 811583942  |
| Programmer | Peter Choi      | 811574558  |

## Instructions to Run It
- Go to queries folder.
- Install docker.
- Run `docker-compose up`
- Open each container and run the queries in each db instance.
	- psql --user postgres project4
	- mysql -p project4 # password is password
- Run SQL scripts:
	- `unoptimized.sql`
	- `add_indexes.sql`
	- `optimized.sql`

## Important notes
If you want to change v8, you need to change it in the view of `optimized.sql` for both postgreSQL and mySQL.