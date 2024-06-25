export interface RestaurantManagerData {
    id: number,
    name: string,
    city: string,
    country: string,
    profilePicture: string,
    shortDescription: string,
    rating: number,
    numberOfReviews: number,
    pictures: {
        id: number,
        picture: string
    }[]
}
