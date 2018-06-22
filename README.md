# search_demo

Endpoints

GET

searchSynonyms(String query) - search synonyms on the description field
getAutoCompleteSuggestions(String query)  - get autocompletion suggestions on the quotes field
searchSoundex(String query)  - search for similar sounding names on the suthor field
getClosestLocations(String zipCode, String distanceInKM)  - get libraries within the given distance from the given zipcode sorted by proximity

getDistinctAutoCompleteSuggestions(String query) - sync call to get a list of distinct autocompletion suggestions ( uses facets) 





