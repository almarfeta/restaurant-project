import {Component, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {RestaurantProfileData} from 'src/app/interfaces/restaurant-profile-data';
import {RestaurantService} from 'src/app/services/restaurant.service';
import {ReviewService} from 'src/app/services/review.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {UpvoteService} from 'src/app/services/upvote.service';

@Component({
  selector: 'app-restaurant-page',
  templateUrl: './restaurant-page.component.html',
  styleUrls: ['./restaurant-page.component.css']
})
export class RestaurantPageComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute,
              private restaurantService: RestaurantService,
              private reviewService: ReviewService,
              private upvoteService: UpvoteService,
              private fb: FormBuilder,
              private messageService: MessageService,
              private confirmationService: ConfirmationService) {
  }

  restaurantId: number = 0;
  restaurantFound: boolean = false;
  restaurant: RestaurantProfileData = {
    name: '',
    city: '',
    country: '',
    shortDescription: '',
    since: '',
    rating: 0,
    numberOfReviews: 0,
    pictures: [],
    profilePicture: '',
    reviews: []
  };
  shownRating: number = 0;

  isFullscreenGallery: boolean = false;
  isAddFormVisible: boolean = false;
  myReviewsVisible: boolean = false;

  public addReviewForm = this.fb.group({
    restaurantId: 0,
    title: '',
    note: '',
    rating: 0,
  });

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      this.restaurantId = +params.get('id')!;
      this.getRestaurantData();
    });
  }

  getRestaurantData() {
    this.restaurantService.getRestaurantById(this.restaurantId).subscribe(
      (response: any) => {
        this.restaurantFound = true;
        this.restaurant = response.data.restaurantProfile;
        this.shownRating = Math.round(this.restaurant.rating);
        this.getReviews();
      },
      (error: any) => {
        this.restaurantFound = false;
      }
    );
  }

  isUserLoggedIn() {
    if (localStorage.getItem('token') !== null) {
      return true;
    }
    return false;
  }

  getReviews() {
    this.reviewService.getReviewsForRestaurant(this.restaurantId).subscribe(
      (response: any) => {
        this.restaurant.reviews = response.data.reviews;
        if (this.isUserLoggedIn()) {
          this.upvoteService.getReviewsUpVotedByUser(this.restaurantId).subscribe(
            (response: any) => {
              this.restaurant.reviews.forEach((review: any) => {
                let flag = false;
                response.data.reviewIds.forEach((reviewId: number) => {
                  if (review.id === reviewId) {
                    review.upVoted = true;
                    flag = true;
                  }
                });
                if (!flag) {
                  review.upVoted = false;
                }
              });
            }
          );
        }
      }
    )
  }

  getMyReviews() {
    this.reviewService.getReviewsForAuthorAndRestaurant(this.restaurantId).subscribe(
      (response: any) => {
        this.restaurant.reviews = response.data.reviews;
      }
    );
  }

  switchReviews() {
    if (this.myReviewsVisible) {
      this.getReviews();
      this.myReviewsVisible = false;
    } else {
      this.getMyReviews();
      this.myReviewsVisible = true;
    }
  }

  onAdd() {
    this.addReviewForm.reset();
    this.addReviewForm.patchValue({
      restaurantId: this.restaurantId
    });
    this.isAddFormVisible = true;
  }

  addReview() {
    this.reviewService.addReview(this.addReviewForm.value).subscribe(
      (response: any) => {
        this.messageService.add({severity: 'success', summary: 'Added', detail: response.message});
        this.isAddFormVisible = false;
        this.addReviewForm.reset();
        this.getRestaurantData();
      },
      (error: any) => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: error.error.errorMessage});
      }
    );
  }

  deleteReview(reviewId: number) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this review?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.reviewService.deleteReview(reviewId).subscribe(
          (response: any) => {
            this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: response.message });
            this.myReviewsVisible = false;
            this.getRestaurantData();
          },
          (error: any) => {
            this.messageService.add({severity: 'error', summary: 'Error', detail: error.error.errorMessage});
          }
        );
      },
      reject: () => {
        this.messageService.add({ severity: 'warn', summary: 'Canceled', detail: 'Action canceled' });
      }
    });
  }

  onUpVote(review: any) {
    if (review.upVoted === true) {
      this.deleteUpVote(review.id);
    } else {
      this.addUpVote(review.id);
    }
  }

  addUpVote(reviewId: number) {
    this.upvoteService.addUpVote(reviewId).subscribe(
      (response: any) => {
        this.getReviews();
      }
    );
  }

  deleteUpVote(reviewId: number) {
    this.upvoteService.deleteUpVote(reviewId).subscribe(
      (response: any) => {
        this.getReviews();
      }
    );
  }
}
