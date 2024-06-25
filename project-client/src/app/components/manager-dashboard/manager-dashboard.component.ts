import {Component, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {RestaurantManagerData} from 'src/app/interfaces/restaurant-manager-data';
import {RestaurantService} from 'src/app/services/restaurant.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {UserService} from 'src/app/services/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-manager-dashboard',
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.css']
})
export class ManagerDashboardComponent implements OnInit {

  constructor(private fb: FormBuilder,
              private restaurantService: RestaurantService,
              private userService: UserService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService,
              private router: Router) {
  }

  public restaurants: RestaurantManagerData[] = [];

  public isAddFormVisible: boolean = false;
  public isUpdateFormVisible: boolean = false;

  public addRestaurantForm = this.fb.group({
    name: '',
    city: '',
    country: '',
    profilePicture: '',
    shortDescription: ''
  });

  public updateRestaurantForm = this.fb.group({
    id: 0,
    name: '',
    shortDescription: '',
    profilePicture: ''
  });

  public newPictureUrl: string = '';

  get updateFormId() {
    return this.updateRestaurantForm.get('id');
  }

  ngOnInit(): void {
    if (localStorage.getItem('token') !== null) {
      this.userService.checkAuthStatus().subscribe(
        (response: any) => {
          this.getRestaurants();
        },
        (error: any) => {
          localStorage.removeItem('token');
          this.router.navigate(["/search-results/all"]);
        }
      );
    }
  }

  getRestaurants() {
    this.restaurantService.getRestaurantsManagedByUser().subscribe(
      (response: any) => {
        this.restaurants = response.data.restaurants;
      }
    );
  }

  onAdd() {
    this.addRestaurantForm.reset();
    this.isUpdateFormVisible = false;
    this.isAddFormVisible = true;
  }

  onUpdate(restaurant: any) {
    this.updateRestaurantForm.reset();
    this.updateRestaurantForm.patchValue({
      id: restaurant.id,
      name: restaurant.name,
      shortDescription: restaurant.shortDescription,
      profilePicture: restaurant.profilePicture
    });
    this.isAddFormVisible = false;
    this.isUpdateFormVisible = true;
  }

  addRestaurant() {
    this.restaurantService.addRestaurant(this.addRestaurantForm.value).subscribe(
      (response: any) => {
        this.messageService.add({severity: 'success', summary: 'Added', detail: response.message});
        this.isAddFormVisible = false;
        this.addRestaurantForm.reset();
        this.getRestaurants();
      },
      (error: any) => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: error.error.errorMessage});
      }
    );
  }

  updateRestaurant() {
    this.restaurantService.updateRestaurant(this.updateRestaurantForm.value).subscribe(
      (response: any) => {
        this.messageService.add({severity: 'info', summary: 'Updated', detail: response.message});
        this.isUpdateFormVisible = false;
        this.updateRestaurantForm.reset();
        this.getRestaurants();
      },
      (error: any) => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: error.error.errorMessage});
      }
    )
  }

  onNewPictureUpload() {
    if (this.newPictureUrl !== '') {
      const formData = new FormData();
      formData.append('restaurantId', this.updateFormId!.value!.toString());
      formData.append('picture', this.newPictureUrl);

      this.restaurantService.addRestaurantPicture(formData).subscribe(
        (response: any) => {
          this.messageService.add({severity: 'info', summary: 'Uploaded', detail: response.message});
          this.isUpdateFormVisible = false;
          this.updateRestaurantForm.reset();
          this.getRestaurants();
        },
        (error: any) => {
          this.messageService.add({severity: 'error', summary: 'Error', detail: error.error.errorMessage});
        }
      );
    } else {
      this.messageService.add({severity: 'warn', summary: 'Update', detail: 'No image selected'});
    }
  }

  deleteRestaurant(restaurantId: number) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this restaurant?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.restaurantService.deleteRestaurant(restaurantId).subscribe(
          (response: any) => {
            this.messageService.add({severity: 'info', summary: 'Confirmed', detail: response.message});
            this.getRestaurants();
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

  deletePicture(restaurantId: number, pictureId: number) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this picture?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.restaurantService.deletePicture(restaurantId, pictureId).subscribe(
          (response: any) => {
            this.messageService.add({severity: 'info', summary: 'Confirmed', detail: response.message});
            this.getRestaurants();
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
