# Local Database Configuration

The project uses a `.env` file for local database configuration.

Create a `.env` file in the project root directory, next to `pom.xml`.

You can use `.env.example` as a template:

```env
DB_URL=jdbc:mariadb://localhost:3306/edujournal
DB_USERNAME=your_username
DB_PASSWORD=your_password
DB_NAME=edujournal
DB_PORT=3306