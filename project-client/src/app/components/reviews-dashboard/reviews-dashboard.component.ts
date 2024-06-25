import {Component, OnInit} from '@angular/core';
import {ReviewService} from 'src/app/services/review.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {UserService} from 'src/app/services/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-reviews-dashboard',
  templateUrl: './reviews-dashboard.component.html'
})
export class ReviewsDashboardComponent implements OnInit {

  constructor(private reviewService: ReviewService,
              private userService: UserService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService,
              private router: Router) {
  }

  reviews: any[] = [];

  ngOnInit(): void {
    if (localStorage.getItem('token') !== null) {
      this.userService.checkAuthStatus().subscribe(
        (response: any) => {
          this.getReviews();
        },
        (error: any) => {
          localStorage.removeItem('token');
          this.router.navigate(["/search-results/all"]);
        }
      );
    }
  }

  getReviews() {
    this.reviewService.getReviewsForAuthor().subscribe(
      (response: any) => {
        this.reviews = response.data.reviews;
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
            this.messageService.add({severity: 'info', summary: 'Confirmed', detail: response.message});
            this.getReviews();
          },
          (error: any) => {
            this.messageService.add({severity: 'error', summary: 'Error', detail: error.error.errorMessage});
          }
        );
      },
      reject: () => {
        this.messageService.add({severity: 'warn', summary: 'Canceled', detail: 'Action canceled'});
      }
    });
  }
}
