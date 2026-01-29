<div align="center">

# 🎬 CineConnect

### The Social Entertainment Discovery Platform

*Reimagining How Students Discover and Experience Entertainment*

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![GraphQL](https://img.shields.io/badge/GraphQL-E10098?style=for-the-badge&logo=graphql&logoColor=white)](https://graphql.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

[Features](#-features) • [Architecture](#-architecture) • [Getting Started](#-getting-started) • [API Documentation](#-api-documentation) • [Contributing](#-contributing)

</div>

---

## 🎯 The Problem

> *"What should we watch tonight?"*

Students today face **decision fatigue** — thousands of movies across Netflix, Prime Video, Disney+, and theaters, yet choosing what to watch remains frustratingly difficult.

**The current experience is broken:**
- 📱 Generic algorithms don't understand your friend group's taste
- 🤷 No way to see what friends are watching or recommending
- 📅 Coordinating movie nights is a logistical nightmare
- ⭐ Star ratings don't capture *why* a movie is worth watching

## 💡 Our Solution

**CineConnect** transforms passive watching into an **interactive, community-driven experience** by combining:

```
🔗 Social Networking + 🎯 Smart Discovery + ⚡ Real-Time Engagement
```

We believe students trust **friends over algorithms**. CineConnect makes entertainment social again.

---

## ✨ Features

### 🍿 Smart Movie Discovery
Explore movies with intelligent, contextual filters:

```graphql
query DiscoverMovies {
  movies(
    filter: { genreSlug: "sci-fi", moodName: "mind-bending" }
    page: 0
    size: 20
  ) {
    content {
      id
      title
      releaseYear
      durationMinutes
      synopsis
      posterUrl
      genres { name slug }
      moods { name }
    }
    pageInfo {
      totalElements
      hasNextPage
    }
  }
}
```

**Filter by:** Genre • Mood • Duration • Language • Search by Title

---

### 👥 Social Recommendation Engine

Build your entertainment network:

| Feature | Description |
|---------|-------------|
| **Follow Friends** | Connect with classmates and see their activity |
| **Shared Watchlists** | Save movies to watch later |
| **Activity Feeds** | Get real-time updates when friends review movies |
| **Friend Reviews** | See what your network thinks before watching |

```graphql
query FriendActivity {
  friendsFeed(page: 0, size: 20) {
    content {
      id
      user { username displayName avatar }
      movie { title posterUrl }
      rating
      reactionTags
      content
      createdAt
    }
    pageInfo { hasNextPage totalElements }
  }
}
```

---

### 🎉 Watch Party Coordinator

Planning group movie nights, simplified:

```
┌─────────────────────────────────────────────────────────────┐
│  🎬 Watch Party: "Weekend Chill"                            │
├─────────────────────────────────────────────────────────────┤
│  👥 Participants: @alex @priya @raj @sneha                  │
│  📅 Scheduled: Saturday 8PM                                 │
│  🎯 Max Participants: 10                                    │
├─────────────────────────────────────────────────────────────┤
│  📊 Movie Suggestions:                                      │
│  ├── "The Grand Budapest Hotel" ████████░░ 4 votes         │
│  ├── "Knives Out"               ██████░░░░ 3 votes         │
│  └── "Palm Springs"             ████░░░░░░ 2 votes         │
├─────────────────────────────────────────────────────────────┤
│  🎟️ Invite Code: ABC123                                    │
│  ✅ Status: SCHEDULED                                       │
└─────────────────────────────────────────────────────────────┘
```

**Capabilities:**
- 🗓️ Schedule watch parties with friends
- 🎬 Suggest movies and vote on what to watch
- 📊 Real-time voting with leaderboard
- 🎟️ Share invite codes to join parties
- 👑 Host controls (start, cancel, select movie)

```graphql
mutation CreateWatchParty {
  createWatchParty(input: {
    title: "Friday Movie Night"
    description: "Let's watch something fun!"
    scheduledAt: "2025-02-01T20:00:00Z"
    maxParticipants: 10
  }) {
    id
    title
    inviteCode
    status
    host { username }
  }
}
```

---

### ⭐ Dynamic Review System

Move beyond simple star ratings with **reaction tags**:

```
┌────────────────────────────────────────┐
│  🎬 Inception (2010)                   │
│  ⭐ 4.5/5 from your friends            │
├────────────────────────────────────────┤
│  Popular Tags:                         │
│  🤯 Mind-Blown (42)                    │
│  🔄 Rewatchable (38)                   │
│  💭 Thought-Provoking (35)             │
│  😢 Made Me Cry (12)                   │
│  😂 Laughed Out Loud (8)               │
└────────────────────────────────────────┘
```

**Available Reaction Tags:**
| Tag | Meaning |
|-----|---------|
| 🤯 `MIND_BLOWN` | Complex, unexpected |
| 💭 `THOUGHT_PROVOKING` | Deep, meaningful |
| 😂 `LAUGHED_OUT_LOUD` | Genuinely funny |
| 😢 `MADE_ME_CRY` | Emotionally powerful |
| 😱 `EDGE_OF_SEAT` | Intense, thrilling |
| ❤️ `HEARTWARMING` | Feel-good vibes |
| 🔄 `REWATCHABLE` | Worth seeing again |
| 💎 `HIDDEN_GEM` | Underrated treasure |
| 🎭 `GREAT_ACTING` | Outstanding performances |
| 🎨 `VISUALLY_STUNNING` | Beautiful cinematography |
| 🎵 `AMAZING_SOUNDTRACK` | Great music |
| 👨‍👩‍👧‍👦 `FAMILY_FRIENDLY` | Safe for all ages |

---

### 📊 Personal Entertainment Dashboard

Visualize your entertainment journey:

```
┌─────────────────────────────────────────────────────────────┐
│  📊 Your CineConnect Dashboard                              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  📝 Reviews Written: 47        👥 Followers: 156           │
│  👀 Following: 89              📋 Watchlist: 23            │
│  🎉 Parties Hosted: 8                                       │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  📝 Recent Reviews:                                         │
│  ├── "Dune: Part Two" ⭐⭐⭐⭐⭐ - Mind-blowing visuals!    │
│  ├── "Poor Things" ⭐⭐⭐⭐ - Unique and bizarre           │
│  └── "Oppenheimer" ⭐⭐⭐⭐⭐ - Masterpiece                 │
│                                                             │
│  🎉 Upcoming Parties:                                       │
│  ├── "Oscar Watch Party" - Feb 15, 8PM                     │
│  └── "Horror Night" - Feb 22, 9PM                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

```graphql
query MyDashboard {
  myDashboard {
    reviewCount
    followerCount
    followingCount
    watchlistCount
    hostedPartiesCount
    recentReviews {
      movie { title }
      rating
      reactionTags
    }
    upcomingParties {
      title
      scheduledAt
      participantCount
    }
  }
}
```

---

## 🏗️ Architecture

### System Overview

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           CLIENT LAYER                                   │
├─────────────────────────────────────────────────────────────────────────┤
│  📱 Mobile App     💻 Web App      🖥️ GraphQL Playground                │
│     (Future)         (Future)         (Built-in)                        │
└────────────────────────────┬────────────────────────────────────────────┘
                             │ GraphQL over HTTPS
                             ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         API GATEWAY                                      │
├─────────────────────────────────────────────────────────────────────────┤
│  🔐 JWT Authentication    📊 Rate Limiting    📝 Request Logging        │
└────────────────────────────┬────────────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    SPRING BOOT 4 APPLICATION                             │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐      │
│  │  GraphQL Layer   │  │  Business Logic  │  │   Data Access    │      │
│  ├──────────────────┤  ├──────────────────┤  ├──────────────────┤      │
│  │ • Controllers    │  │ • UserService    │  │ • JPA Repos      │      │
│  │ • @QueryMapping  │◄─┤ • MovieService   │◄─┤ • Custom Queries │      │
│  │ • DataLoaders    │  │ • SocialService  │  │ • Specifications │      │
│  │ • Input Types    │  │ • PartyService   │  │ • Pagination     │      │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘      │
│                                                                          │
└────────────────────────────┬────────────────────────────────────────────┘
                             │
          ┌──────────────────┼──────────────────┐
          ▼                  ▼                  ▼
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│   PostgreSQL 16  │ │     Redis 7      │ │   External APIs  │
├──────────────────┤ ├──────────────────┤ ├──────────────────┤
│ • Users          │ │ • Session Cache  │ │ • TMDB (Future)  │
│ • Movies         │ │ • Popular Movies │ │ • JustWatch      │
│ • Reviews        │ │ • Stats Cache    │ │   (Future)       │
│ • Watch Parties  │ │ • Genre/Mood     │ │                  │
│ • Social Graph   │ │   Reference Data │ │                  │
└──────────────────┘ └──────────────────┘ └──────────────────┘
```

### Tech Stack

| Layer | Technology | Justification |
|-------|------------|---------------|
| **Runtime** | Java 25 | Latest with virtual threads, pattern matching |
| **Framework** | Spring Boot 4.0.2 | Production-ready, excellent GraphQL support |
| **API** | Spring for GraphQL | Schema-first, efficient data fetching |
| **Database** | PostgreSQL 16 | ACID compliance, JSON support, full-text search |
| **Cache** | Redis 7 | Session management, popular content caching |
| **Auth** | Spring Security + JWT | Stateless, scalable authentication |
| **Migrations** | Flyway | Version-controlled database schema |
| **Build** | Maven | Reliable dependency management |
| **Testing** | JUnit 5 + Testcontainers | Integration tests with real databases |

### Data Model

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    User     │       │    Movie    │       │   Review    │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id          │       │ id          │       │ id          │
│ username    │       │ title       │       │ userId   ───┼──┐
│ email       │       │ releaseYear │       │ movieId  ───┼──┼──┐
│ displayName │       │ duration    │       │ rating      │  │  │
│ avatar      │       │ synopsis    │       │ content     │  │  │
│ bio         │       │ posterUrl   │       │ reactionTags│  │  │
│ createdAt   │       │ backdropUrl │       │ containsSpo │  │  │
└──────┬──────┘       │ language    │       │ createdAt   │  │  │
       │              │ tmdbId      │       └─────────────┘  │  │
       │              │ imdbId      │                        │  │
       │              └──────┬──────┘                        │  │
       │                     │                               │  │
       │              ┌──────┴──────┐                        │  │
       │              ▼             ▼                        │  │
       │        ┌─────────┐  ┌─────────┐                    │  │
       │        │  Genre  │  │  Mood   │                    │  │
       │        ├─────────┤  ├─────────┤                    │  │
       │        │ id      │  │ id      │                    │  │
       │        │ name    │  │ name    │                    │  │
       │        │ slug    │  └─────────┘                    │  │
       │        └─────────┘                                 │  │
       │                                                    │  │
       ▼                                                    │  │
┌─────────────┐       ┌─────────────────┐                  │  │
│  Follower   │       │   WatchParty    │◄─────────────────┘  │
├─────────────┤       ├─────────────────┤                     │
│ followerId  │       │ id              │                     │
│ followingId │       │ title           │                     │
│ createdAt   │       │ description     │                     │
└─────────────┘       │ hostId       ───┼─────────────────────┘
                      │ selectedMovie   │
┌─────────────┐       │ scheduledAt     │
│  Watchlist  │       │ status          │
├─────────────┤       │ inviteCode      │
│ id          │       │ maxParticipants │
│ userId      │       └────────┬────────┘
│ movieId     │                │
│ addedAt     │                ▼
└─────────────┘       ┌─────────────────┐
                      │  Participant    │
                      ├─────────────────┤
                      │ id              │
                      │ partyId         │
                      │ userId          │
                      │ status          │
                      │ joinedAt        │
                      └─────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Version | Installation |
|-------------|---------|--------------|
| Java | 25+ | [SDKMAN](https://sdkman.io/) or [Adoptium](https://adoptium.net/) |
| Docker | 20+ | [Docker Desktop](https://www.docker.com/products/docker-desktop/) |
| Maven | 3.9+ | Included via wrapper (`./mvnw`) |

### Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/dbc2201/cine-connect-graphql-demo.git
cd cine-connect-graphql-demo

# 2. Start dependencies (PostgreSQL + Redis)
docker-compose up -d

# 3. Run the application
./mvnw spring-boot:run

# 4. Open GraphQL Playground
open http://localhost:8080/graphiql
```

### Environment Configuration

The application uses Spring profiles. Default development settings are in `application-dev.yml`:

```yaml
# Database (via Docker Compose)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cineconnect
    username: cineconnect
    password: cineconnect_dev

  # Redis
  data:
    redis:
      host: localhost
      port: 6379

# JWT Configuration
jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key}
  access-token-validity-ms: 3600000     # 1 hour
  refresh-token-validity-ms: 604800000  # 7 days
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | PostgreSQL host |
| `DB_PORT` | 5432 | PostgreSQL port |
| `DB_NAME` | cineconnect | Database name |
| `DB_USERNAME` | cineconnect | Database user |
| `DB_PASSWORD` | cineconnect_dev | Database password |
| `REDIS_HOST` | localhost | Redis host |
| `REDIS_PORT` | 6379 | Redis port |
| `JWT_SECRET` | (dev default) | JWT signing key (256+ bits) |

### Docker Compose Services

```yaml
services:
  postgres:
    image: postgres:16-alpine
    ports: ["5432:5432"]

  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]
```

---

## 📚 API Documentation

### Authentication

```graphql
# Register a new user
mutation Register {
  register(input: {
    username: "moviebuff"
    email: "buff@campus.edu"
    password: "securePassword123"
    displayName: "Movie Buff"
  }) {
    accessToken
    refreshToken
    user { id username displayName }
  }
}

# Login
mutation Login {
  login(input: {
    usernameOrEmail: "moviebuff"
    password: "securePassword123"
  }) {
    accessToken
    refreshToken
    user { id username avatar }
  }
}

# Refresh token
mutation RefreshToken {
  refreshToken(token: "your-refresh-token") {
    accessToken
    refreshToken
  }
}
```

### Movie Queries

```graphql
# Get all movies with pagination
query AllMovies {
  movies(page: 0, size: 20) {
    content {
      id
      title
      releaseYear
      durationMinutes
      synopsis
      posterUrl
      genres { id name slug }
      moods { id name }
    }
    pageInfo {
      totalPages
      totalElements
      hasNextPage
      hasPreviousPage
    }
  }
}

# Filter by genre
query MoviesByGenre {
  movies(filter: { genreSlug: "action" }, page: 0, size: 10) {
    content { id title releaseYear }
  }
}

# Filter by mood
query MoviesByMood {
  movies(filter: { moodName: "uplifting" }, page: 0, size: 10) {
    content { id title moods { name } }
  }
}

# Search movies
query SearchMovies {
  searchMovies(query: "inception", page: 0, size: 10) {
    content { id title synopsis }
  }
}

# Get single movie with reviews
query MovieDetails {
  movie(id: "1") {
    id
    title
    releaseYear
    synopsis
    genres { name }
    moods { name }
  }
  reviewsForMovie(movieId: "1", page: 0, size: 5) {
    content {
      user { username avatar }
      rating
      content
      reactionTags
      createdAt
    }
  }
}
```

### Review Mutations

```graphql
# Add a review (requires auth)
mutation AddReview {
  createReview(input: {
    movieId: "1"
    rating: 5
    content: "Absolutely mind-bending! Nolan at his finest."
    reactionTags: [MIND_BLOWN, THOUGHT_PROVOKING, REWATCHABLE]
    containsSpoiler: false
  }) {
    id
    rating
    reactionTags
    createdAt
  }
}

# Update a review
mutation UpdateReview {
  updateReview(id: "1", input: {
    rating: 4
    content: "Updated thoughts after second viewing..."
  }) {
    id
    rating
    content
    updatedAt
  }
}

# Delete a review
mutation DeleteReview {
  deleteReview(id: "1")
}
```

### Social Features

```graphql
# Follow a user
mutation FollowUser {
  follow(userId: "2")
}

# Unfollow a user
mutation UnfollowUser {
  unfollow(userId: "2")
}

# Add to watchlist
mutation AddToWatchlist {
  addToWatchlist(movieId: "1") {
    id
    movie { title posterUrl }
    addedAt
  }
}

# Remove from watchlist
mutation RemoveFromWatchlist {
  removeFromWatchlist(movieId: "1")
}

# Get my watchlist
query MyWatchlist {
  myWatchlist(page: 0, size: 20) {
    content {
      id
      movie { id title posterUrl releaseYear }
      addedAt
    }
  }
}

# Get friends' activity feed
query FriendsFeed {
  friendsFeed(page: 0, size: 20) {
    content {
      id
      user { username displayName avatar }
      movie { title posterUrl }
      rating
      reactionTags
      content
      createdAt
    }
  }
}
```

### Watch Party Operations

```graphql
# Create a watch party
mutation CreateParty {
  createWatchParty(input: {
    title: "Friday Movie Night"
    description: "Let's watch something epic!"
    scheduledAt: "2025-02-01T20:00:00Z"
    maxParticipants: 10
  }) {
    id
    title
    inviteCode
    status
    host { username }
  }
}

# Join with invite code
mutation JoinParty {
  joinWatchParty(inviteCode: "ABC123") {
    id
    title
    host { username }
    participantCount
  }
}

# Suggest a movie
mutation SuggestMovie {
  suggestMovie(partyId: "1", movieId: "5") {
    id
    movie { title posterUrl }
    suggestedBy { username }
    voteCount
  }
}

# Vote for a movie
mutation VoteForMovie {
  voteForMovie(partyId: "1", suggestionId: "3")
}

# Start the party (host only)
mutation StartParty {
  startWatchParty(partyId: "1") {
    id
    status
    selectedMovie { title }
    startedAt
  }
}

# Get party details
query PartyDetails {
  watchParty(id: "1") {
    id
    title
    description
    status
    scheduledAt
    host { username avatar }
    participants {
      user { username avatar }
      status
    }
    movieSuggestions {
      movie { title posterUrl }
      voteCount
      voters { username }
    }
    selectedMovie { title }
  }
}

# Get my parties
query MyParties {
  myWatchParties {
    id
    title
    status
    scheduledAt
    participantCount
  }
}
```

### Dashboard & Stats

```graphql
# Personal dashboard (requires auth)
query MyDashboard {
  myDashboard {
    reviewCount
    followerCount
    followingCount
    watchlistCount
    hostedPartiesCount
    recentReviews {
      movie { title }
      rating
      createdAt
    }
    upcomingParties {
      title
      scheduledAt
      participantCount
    }
  }
}

# Platform statistics (public)
query PlatformStats {
  platformStats {
    totalUsers
    totalMovies
    totalReviews
    scheduledParties
    liveParties
  }
}
```

---

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run with verbose output
./mvnw test -X

# Run specific test class
./mvnw test -Dtest=MovieServiceTest

# Run integration tests only
./mvnw test -Dtest="*IntegrationTest"
```

### Test Categories

| Category | Description | Count |
|----------|-------------|-------|
| Unit Tests | Domain entities, services | 35+ |
| Repository Tests | JPA queries with Testcontainers | 15+ |
| Integration Tests | GraphQL endpoints | 12+ |

**Total: 62 tests passing**

---

## 📁 Project Structure

```
cine-connect-graphql-demo/
├── src/
│   ├── main/
│   │   ├── java/io/github/dbc2201/cineconnectgraphqldemo/
│   │   │   ├── CineConnectGraphqlDemoApplication.java
│   │   │   ├── config/                    # Configuration
│   │   │   │   ├── DataLoaderConfig.java  # N+1 prevention
│   │   │   │   ├── GraphQlConfig.java     # Scalars, settings
│   │   │   │   ├── RedisConfig.java       # Cache setup
│   │   │   │   └── SecurityConfig.java    # JWT security
│   │   │   ├── domain/                    # JPA Entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Movie.java
│   │   │   │   ├── Review.java
│   │   │   │   ├── Genre.java
│   │   │   │   ├── Mood.java
│   │   │   │   ├── ReactionTag.java       # Enum
│   │   │   │   ├── Follower.java
│   │   │   │   ├── Watchlist.java
│   │   │   │   ├── WatchParty.java
│   │   │   │   ├── WatchPartyParticipant.java
│   │   │   │   └── WatchPartyMovieSuggestion.java
│   │   │   ├── repository/                # Data Access
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── MovieRepository.java
│   │   │   │   ├── ReviewRepository.java
│   │   │   │   ├── FollowerRepository.java
│   │   │   │   ├── WatchlistRepository.java
│   │   │   │   └── WatchParty*Repository.java
│   │   │   ├── service/                   # Business Logic
│   │   │   │   ├── UserService.java
│   │   │   │   ├── MovieService.java
│   │   │   │   ├── ReviewService.java
│   │   │   │   ├── SocialService.java
│   │   │   │   ├── WatchPartyService.java
│   │   │   │   └── DashboardService.java
│   │   │   ├── graphql/                   # GraphQL Controllers
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── MovieController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   ├── SocialController.java
│   │   │   │   ├── WatchPartyController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── input/                 # Input DTOs
│   │   │   │   │   ├── RegisterInput.java
│   │   │   │   │   ├── LoginInput.java
│   │   │   │   │   ├── CreateMovieInput.java
│   │   │   │   │   ├── CreateReviewInput.java
│   │   │   │   │   └── CreateWatchPartyInput.java
│   │   │   │   └── type/                  # Response DTOs
│   │   │   │       ├── AuthPayload.java
│   │   │   │       ├── MovieConnection.java
│   │   │   │       └── PageInfo.java
│   │   │   └── security/                  # Auth Components
│   │   │       ├── JwtTokenProvider.java
│   │   │       ├── JwtAuthFilter.java
│   │   │       └── CineConnectUserDetailsService.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── graphql/                   # GraphQL Schemas
│   │       │   ├── schema.graphqls        # Base types
│   │       │   ├── user.graphqls
│   │       │   ├── movie.graphqls
│   │       │   ├── review.graphqls
│   │       │   ├── auth.graphqls
│   │       │   ├── social.graphqls
│   │       │   ├── watchparty.graphqls
│   │       │   └── dashboard.graphqls
│   │       └── db/migration/              # Flyway Migrations
│   │           ├── V1__initial_schema.sql
│   │           ├── V2__create_reviews_table.sql
│   │           ├── V3__add_social_features.sql
│   │           └── V4__add_watch_party.sql
│   └── test/
│       └── java/.../
│           ├── domain/                    # Entity tests
│           ├── repository/                # Repository tests
│           └── graphql/                   # Integration tests
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🎖️ Key Implementation Highlights

### ✅ DataLoaders for N+1 Prevention
```java
@Configuration
public class DataLoaderConfig {
    public DataLoaderConfig(BatchLoaderRegistry registry,
                            UserRepository userRepository) {
        registry.forTypePair(Long.class, User.class)
            .withName("userLoader")
            .registerMappedBatchLoader((userIds, env) -> {
                Map<Long, User> usersById = userRepository
                    .findAllByIdIn(Set.copyOf(userIds))
                    .stream()
                    .collect(Collectors.toMap(User::getId, identity()));
                return Mono.just(usersById);
            });
    }
}
```

### ✅ Redis Caching with Custom TTLs
```java
@Configuration
@EnableCaching
public class RedisConfig {
    // Movies: 1 hour TTL
    // Users: 15 minutes TTL
    // Genres/Moods: 24 hours TTL
    // Stats: 5 minutes TTL
}
```

### ✅ JWT Authentication
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session ->
            session.sessionCreationPolicy(STATELESS))
        .addFilterBefore(jwtAuthFilter,
            UsernamePasswordAuthenticationFilter.class)
        .build();
}
```

### ✅ Schema-First GraphQL
All types defined in `.graphqls` files with Spring's `@QueryMapping`, `@MutationMapping`, and `@SchemaMapping` annotations for type-safe resolution.

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

```bash
# 1. Fork and clone
git clone https://github.com/YOUR_USERNAME/cine-connect-graphql-demo.git

# 2. Create feature branch
git checkout -b feature/amazing-feature

# 3. Make changes and test
./mvnw test

# 4. Commit with conventional commits
git commit -m "feat: add mood-based recommendations"

# 5. Push and create PR
git push origin feature/amazing-feature
```

### Commit Convention

| Type | Description |
|------|-------------|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | Documentation |
| `style` | Formatting |
| `refactor` | Code restructuring |
| `test` | Adding tests |
| `chore` | Maintenance |

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Built with ❤️ using Spring Boot 4 and GraphQL**

*Transforming entertainment from solo scrolling to shared experiences*

[⬆ Back to Top](#-cineconnect)

</div>
