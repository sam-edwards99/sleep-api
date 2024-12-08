Hello,

I realize now that I misunderstood the PR-merging requirements for this assignment and I did not in fact create and merge pull requests 
fully with a live-production application in mind.

If I had done so, I would have broken my PR's up in a bottom-up approach like the following...

1. Set up flyway tables and data. Create Entities, DTOs, Mappers and Repositories to be able to connect to and use said data tables.
   Also would have created associated unit tests for these classes where needed.
2. Create Service layer and associated tests. Also add some exception handling.
3. Build Controller layer and associated tests. This would also include DTO Validations, Serialization changes, and associated exception handling.
   This way the new features would only become accessible for users/clients once the controllers were created (and possibly enable via feature flag, etc)

Thanks for taking the time to review this code,
Sam
