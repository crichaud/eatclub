# Task 1: Restaurant Deals API

An API that returns a list of all the available restaurant deals that are active at a specified time of day.

## Details
- The API takes a single parameter named `timeOfDay` as a string (e.g., `10:30` or `3:00pm`).
- **API Endpoint:** [http://localhost:8080/api/v1/deals?timeOfDay=](http://localhost:8080/api/v1/deals?timeOfDay=)

## Logic Rules Applied
- **Specific Deal Hours:** If a deal has its own open/close or start/end times, those are used.
- **Default Restaurant Hours:** If a deal lacks specific times, it inherits the restaurant's open and close times.
- **Boundary Condition:** The deal is active if `open <= timeOfDay < close`.

## Example Data Source
[https://eccdn.com.au/misc/challengedata.json](https://eccdn.com.au/misc/challengedata.json)

## Results
### When `timeOfDay = 3:00pm`
Returns 5 deals:
- **Masala Kitchen:** 50% discount (Deal 0000) — Starts exactly at 3:00pm.
- **Masala Kitchen:** 40% discount (Deal 1111) — Inherits restaurant open at 3:00pm.
- **ABC Chicken:** 30% discount (Deal 0000) — Restaurant open from 12pm to 11pm.
- **ABC Chicken:** 20% discount (Deal 1111) — Restaurant open from 12pm to 11pm.
- **Kekou:** 10% discount (Deal 0000) — Deal starts at 2:00pm.

### When `timeOfDay = 6:00pm`
Returns 9 deals:
- **Masala Kitchen:** Discounts of 50% and 40%.
- **ABC Chicken:** Discounts of 30% and 20%.
- **Vrindavan:** 10% discount (Deal 0000) — Starts at 3:00pm.
- **Kekou:** Discounts of 10% and 15%.
- **Gyoza Gyoza:** Discounts of 25% and 15%
- *(Note: OzzyThai is excluded as it closes at3:00 pm)*

### When `timeOfDay =9:00 pm`
Returns 4 deals:
- **ABC Chicken:** Discounts of 30 % and 20 %
- **Gyoza Gyoza:** Discounts of 25% and 15%
*(Note : Masala Kitchen , Vrindavan , and Kekou deals expire exactly at9 :00 pm and are typically excluded at the strike of the closing hour).* 

# Task2:
API that calculates the "peak" time window during which most deals are available. This API does not take any parameters.

## Endpoint:
http://localhost:8080/api/v1/peak-window

## Response Format:
bash{
 "peakTimeStart": "5:00pm",
 "peakTimeEnd": "9:00pm"
}")



