# Meta Post Performance Analyzer

Analyze your Facebook Page's last 20 posts using the Meta Graph API. Displays a clean HTML report with top posts by engagement, best posting day, and an analytics summary.

Built with **Java 21 + Spring Boot 3 + Thymeleaf**.

## Features

- Fetches last 20 posts (message, date, likes, comments) via Meta Graph API
- Identifies **top 3 posts** by engagement (likes + comments)
- Calculates which **day of the week** gets the highest average likes
- Generates a natural-language **summary** of findings
- Clean HTML report page
- Terminal-friendly structure — also works as a plain data service

## Prerequisites

- Java 17+ (tested on Java 21)
- A **Meta for Developers** account with a test app and a Facebook Page
- An **Access Token** with `pages_read_engagement` and `pages_show_list` permissions

## Setup

### 1. Clone and configure

```bash
git clone https://github.com/isameddin35/meta-post-analyser.git
cd meta-post-analyser
```

### 2. Environment variables

Copy the example env file and fill in your credentials:

```bash
cp .env.example .env
```

Edit `.env`:

```
ACCESS_TOKEN=your_facebook_access_token_here
PAGE_ID=your_facebook_page_id_here
```

> **Note:** The access token must have `pages_read_engagement` and `pages_show_list` permissions.

### 3. Run locally

```bash
./gradlew bootRun
```

Open http://localhost:8080 in your browser.

To run the JAR directly:

```bash
./gradlew build
java -jar build/libs/meta-post-analyser-1.0.0.jar
```

### 4. Build only

```bash
./gradlew build
```

## How to Get a Meta Access Token

1. Go to [developers.facebook.com](https://developers.facebook.com) → **My Apps** → **Create App** (choose **Business** type)
2. Open **Graph API Explorer** (under Tools)
3. Select your app and choose **User Token**
4. Add permissions: `pages_read_engagement`, `pages_show_list`
5. Click **Generate Access Token**
6. Call `GET /me/accounts` to find your Page ID
7. Copy the token and Page ID into `.env`

## Deployment (Railway)

[![Deploy on Railway](https://railway.app/button.svg)](https://railway.app/new)

### Manual steps

1. Push this repo to GitHub
2. Create a new project on [Railway](https://railway.app)
3. Select **Deploy from GitHub repo**
4. Set the following **Environment Variables** in Railway dashboard → Variables:
   - `ACCESS_TOKEN` — your Facebook access token
   - `PAGE_ID` — your Facebook Page ID
5. Set the **Build Command** to `./gradlew build`
6. Set the **Start Command** to `java -jar build/libs/meta-post-analyser-1.0.0.jar`
7. Railway auto-exposes `$PORT` — the app binds to it by default

## Project Structure

```
meta-post-analyser/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew / gradlew.bat
├── .env.example
├── .gitignore
├── README.md
└── src/main/
    ├── java/com/metaanalyser/
    │   ├── MetaAnalyserApplication.java
    │   ├── model/
    │   │   ├── Post.java
    │   │   └── AnalysisResult.java
    │   ├── service/
    │   │   ├── MetaGraphService.java      # REST calls to Graph API
    │   │   └── AnalyticsService.java      # Engagement analysis
    │   └── controller/
    │       └── ReportController.java      # GET / -> report page
    └── resources/
        ├── application.yml
        └── templates/
            └── report.html                # Thymeleaf report template
```

## Analysis Logic

| Metric | Method |
|---|---|
| **Engagement** | `likes + comments` per post |
| **Top 3** | Sort descending by engagement, take first 3 |
| **Best day** | Group posts by `DayOfWeek`, average likes, pick max |
| **Summary** | Combines best day, top post engagement, and overall average |

## Sample Output

The report page shows:

- **Analytics Summary** — natural-language overview
- **Top 3 Posts** — highlighted cards with engagement breakdown
- **Best Posting Day** — weekday with highest average likes
- **All Posts** — sortable table of the last 20 posts

## License

MIT
