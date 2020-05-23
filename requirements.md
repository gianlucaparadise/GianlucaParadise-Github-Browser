# Requirements Notes

This is a quick summary from the Requirements pdf document.

## UI requirements

- [ ] Login Page
- [ ] Home tab
    - [ ] List of Authenticated User's repos as [cards](https://material.io/components/cards​)
- [ ] Search tab
    - [ ] Search for either repositories or users
- [ ] Repository detail
    - [ ] Detailed view of repository
    - [ ] Button to "star" the repo
- [ ] User detail
    - [ ] Detailed view of user
    - [ ] Number of followers and following

## API and Data requirements

Github API to be integrated:

- [x] [Github API V4](​https://developer.github.com/v4/​)
- [ ] Github Login
- [x] Repo for logged user
    - [ ] Cached in a DB for offline check
- [x] Search repo
- [x] Search users
- [ ] "Star" a repository

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

- [ ] Public Github Repository
- [ ] Kotlin
- [ ] Coroutines or Rx
- [ ] Dagger2
- [ ] `minSdkVersion 21`
- [ ] No outdated libraries
- [ ] No hardcoded strings
- [ ] Meaningful commits
- [ ] Junit+Dagger for testing
- [ ] At least one Espresso integration test
- [ ] Use:
    - [x] networking: OkHttp, Retrofit, Rx or Coroutines
        - Apollo has been chosen because it's the best client for GraphQL backend
    - [ ] view binding: View Binding
    - [ ] image loading: Picasso, Android Universal Image Loader, Fresco, Glide