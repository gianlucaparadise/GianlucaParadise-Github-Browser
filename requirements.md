# Requirements Notes

This is a quick summary from the Requirements pdf document.

## UI requirements

- [x] Login Page
- [x] Home tab
    - [x] List of Authenticated User's repos as [cards](https://material.io/components/cards​)
- [x] Search tab
    - [x] Search for either repositories or users
- [x] Repository detail
    - [x] Detailed view of repository
    - [x] Button to "star" the repo
- [x] User detail
    - [x] Detailed view of user
    - [x] Number of followers and following
- [ ] Nice UI with animations

## API and Data requirements

Github API to be integrated:

- [x] [Github API V4](​https://developer.github.com/v4/​)
- [x] Github Login
- [x] Repo for logged user
    - [x] Cached in a DB for offline check
- [x] Search repo
- [x] Search users
- [x] "Star" a repository

Models:

- [x] [Repository](app/src/main/java/com/gianlucaparadise/githubbrowser/data/Repository.kt):
    - Title
    - Short Desc
    - Primary Programming Lang
    - Stars
    - Owner

- [x] [User](app/src/main/java/com/gianlucaparadise/githubbrowser/data/User.kt):
    - Icon
    - Name
    - Login Id
    - Bio
    - Followers number
    - Following number

## Guidelines

- [x] Public Github Repository
- [x] Kotlin
- [x] Coroutines or Rx
- [ ] Dagger2
- [x] `minSdkVersion 21`
- [x] No outdated libraries
- [x] No hardcoded strings
- [x] Meaningful commits
- [ ] Junit+Dagger for testing
- [ ] At least one Espresso integration test
- [x] Use:
    - [x] networking: OkHttp, Retrofit, Rx or Coroutines
        - Apollo has been chosen because it's the best client for a GraphQL backend
    - [x] view binding: View Binding
    - [x] image loading: Picasso, Android Universal Image Loader, Fresco, Glide

## Future Roadmap

- [ ] Pull to refresh
- [x] Avoid DataSourceFactory instance retain in GitHubRepository
- [x] Network Error UI
- [ ] Call in progress UI
- [x] Disable star button while call in progress
- [x] Fix login webview keyboard
- [ ] Better Login flow
- [ ] Dagger
- [ ] Shared Element Transitions master-detail