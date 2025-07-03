# Vincent Raerek's Portfolio Website

A minimalist personal portfolio website built with Phoenix Framework, displaying professional information and contact links.

## Overview

This project is a simple, single-page Phoenix application that serves as a personal portfolio and resume website. It features:

- Professional profile display with photo
- Downloadable resume (PDF)
- Links to professional profiles (GitHub, LinkedIn, StackOverflow)
- Clean, responsive design using Tailwind CSS

## Tech Stack

- **Backend**: Elixir 1.14+ with Phoenix Framework 1.7.21
- **Frontend**: Phoenix HTML/HEEx templates with Tailwind CSS
- **Asset Building**: ESBuild for JavaScript, Tailwind CLI for CSS
- **Database**: PostgreSQL (configured but not actively used)
- **Deployment**: Fly.io with Docker containerization

## Prerequisites

- Elixir ~> 1.14
- Erlang/OTP 26
- Node.js (for asset compilation)
- PostgreSQL (if enabling database features)
- Docker (for deployment)
- Fly CLI (for deployment to Fly.io)

## Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd blog
   ```

2. **Install dependencies**
   ```bash
   mix deps.get
   ```

3. **Install Node.js dependencies**
   ```bash
   cd assets && npm install
   cd ..
   ```

4. **Set up the database** (optional - not required for current functionality)
   ```bash
   mix ecto.setup
   ```

5. **Start the Phoenix server**
   ```bash
   mix phx.server
   ```

   Or run inside IEx:
   ```bash
   iex -S mix phx.server
   ```

6. **Visit the application**
   - Navigate to [`http://localhost:4000`](http://localhost:4000)
   - Development dashboard available at [`http://localhost:4000/dashboard`](http://localhost:4000/dashboard)

## Project Structure

```
blog/
├── assets/           # Frontend assets (CSS, JS)
├── config/           # Application configuration
├── lib/
│   ├── blog/         # Business logic (minimal)
│   └── blog_web/     # Web layer (controllers, templates, etc.)
├── priv/
│   ├── static/       # Static files (images, PDFs)
│   └── repo/         # Database migrations (none currently)
├── test/             # Test files
├── Dockerfile        # Docker configuration
├── fly.toml          # Fly.io deployment configuration
└── mix.exs           # Project dependencies and configuration
```

## Configuration

### Environment Variables

Create a `.env` file or set these environment variables:

```bash
# Required for production
SECRET_KEY_BASE=<generate-with-mix-phx.gen.secret>
PHX_HOST=<your-domain-or-fly-app-name.fly.dev>

# Optional database configuration
DATABASE_URL=<postgresql-connection-string>
```

### Updating Content

To update the portfolio content:

1. **Profile Image**: Replace `/priv/static/images/yosemite.jpg`
2. **Resume PDF**: Replace `/priv/static/docs/resume.pdf`
3. **Social Links**: Edit `/lib/blog_web/controllers/page_html/home.html.heex`

## Deployment to Fly.io

### Initial Setup

1. **Install Fly CLI**
   ```bash
   curl -L https://fly.io/install.sh | sh
   ```

2. **Login to Fly**
   ```bash
   fly auth login
   ```

3. **Create the app** (if not already created)
   ```bash
   fly apps create withered-forest-1852
   ```

4. **Set secrets**
   ```bash
   fly secrets set SECRET_KEY_BASE=$(mix phx.gen.secret)
   ```

### Deploy

1. **Deploy the application**
   ```bash
   fly deploy
   ```

2. **Check deployment status**
   ```bash
   fly status
   ```

3. **View logs**
   ```bash
   fly logs
   ```

### Deployment Configuration Details

The `fly.toml` file configures:
- **Auto-scaling**: Scales down to 0 machines when not in use (cost-effective)
- **Health checks**: Monitors the root path every 10 seconds
- **HTTPS**: Automatically enforces HTTPS
- **Region**: Deployed to `sea` (Seattle) region by default

### Managing the Deployment

```bash
# Scale the application
fly scale count 1  # Ensure at least one instance running
fly scale count 0  # Allow scaling to zero

# SSH into the running instance
fly ssh console

# Open the application in browser
fly open

# View recent deployments
fly releases
```

## Development Notes

### Running Tests
```bash
mix test
```

### Code Formatting
```bash
mix format
```

### Building Assets
Assets are automatically built in development. For production builds:
```bash
MIX_ENV=prod mix assets.deploy
```

### Adding Database Features
The project has Ecto and PostgreSQL configured but not actively used. To add database features:

1. Create migrations: `mix ecto.gen.migration <migration_name>`
2. Update `lib/blog/application.ex` to start the Repo supervisor
3. Provision a PostgreSQL database on Fly.io: `fly postgres create`

## Maintenance

### Updating Dependencies
```bash
mix deps.update --all
cd assets && npm update
```

### Monitoring
- Production logs: `fly logs`
- Application metrics: `fly dashboard`
- Local development dashboard: `http://localhost:4000/dashboard`

## License

[Add your license information here]

## Contact

- GitHub: [https://github.com/vaer-k](https://github.com/vaer-k)
- LinkedIn: [https://www.linkedin.com/in/vincent-raerek/](https://www.linkedin.com/in/vincent-raerek/)
- StackOverflow: [https://stackoverflow.com/users/1154287/vaer-k](https://stackoverflow.com/users/1154287/vaer-k)