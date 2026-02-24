# Movie ratings example using tuples with loops
movies = ["Zootopia 2", "Avatar: Fire and Ash", "Kokuho"]
ratings = [8.7, 9.1, 7.9]

print("zip example: Movie ratings")
for movie, rating in zip(movies, ratings):
    print(f"{movie} rating:", rating)

print("\nenumerate example: Movie ranking")
for index, movie in enumerate(movies, start=1):
    print(f"Rank {index}:", movie)

print("\ndictionary items() example: Recommendation status")

recommendation = {
    "Zootopia 2": True,
    "Avatar: Fire and Ash": True,
    "Kokuho": False
}

for title, is_recommended in recommendation.items():
    status = "Recommended" if is_recommended else "Not Recommended"
    print(title, ":", status)