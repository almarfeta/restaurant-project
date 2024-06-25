export interface RestaurantProfileData {
    name: string,
    city: string,
    country: string,
    shortDescription: string,
    since: string,
    rating: number,
    numberOfReviews: number,
    pictures: {
        id: number,
        picture: string
    }[],
    profilePicture: string,
    reviews: any
}
