import {Component, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {RegisterUserData} from '../../interfaces/register-user-data';
import {LoginUserData} from '../../interfaces/login-user-data';
import {UserService} from 'src/app/services/user.service';
import {ProfileUserData} from 'src/app/interfaces/profile-user-data';
import {MessageService} from 'primeng/api';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {

  constructor(private fb: FormBuilder,
              private messageService: MessageService,
              private authService: AuthService,
              private userService: UserService,
              private router: Router) {
  }

  isSignInVisible = false;
  isProfileVisible = false;
  isLogin = true;
  signInModalHeader = "Login";
  signInDividerMessage = "Don't have an account?"
  switchButtonMessage = "Go to Register";

  loginForm = this.fb.group({
    email: '',
    password: ''
  });

  registerForm = this.fb.group({
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  });

  profileUserData: ProfileUserData = {
    email: '',
    role: '',
    profileId: 0,
    firstName: '',
    lastName: '',
    joinDate: ''
  }

  searchInput: string = '';

  get loginEmail() {
    return this.loginForm.get('email');
  }

  get loginPassword() {
    return this.loginForm.get('password');
  }

  get registerFirstName() {
    return this.registerForm.get('firstName');
  }

  get registerLastName() {
    return this.registerForm.get('lastName');
  }

  get registerEmail() {
    return this.registerForm.get('email');
  }

  get registerPassword() {
    return this.registerForm.get('password');
  }

  ngOnInit(): void {
    if (localStorage.getItem('token') !== null) {
      this.userService.checkAuthStatus().subscribe(
        (response: any) => {
          this.getUserData();
        },
        (error: any) => {
          localStorage.removeItem('token');
        }
      );
    }
  }

  isAuthenticated() {
    if (localStorage.getItem('token') !== null) {
      return true;
    }
    return false;
  }

  showSignIn() {
    this.isSignInVisible = true;
  }

  showProfile() {
    this.isProfileVisible = true;
  }

  signInSwitch() {
    if (this.isLogin) {
      this.clearSignInFields();
      this.isLogin = false;
      this.signInModalHeader = "Register";
      this.signInDividerMessage = "Already have an account?";
      this.switchButtonMessage = "Go to Login";
    } else {
      this.clearSignInFields();
      this.isLogin = true;
      this.signInModalHeader = "Login";
      this.signInDividerMessage = "Don't have an account?";
      this.switchButtonMessage = "Go to Register";
    }
  }

  clearSignInFields() {
    if (this.isLogin) {
      this.loginForm.reset();
    } else {
      this.registerForm.reset();
    }
  }

  resetModal() {
    this.clearSignInFields();
    this.isLogin = true;
    this.signInModalHeader = "Login";
    this.signInDividerMessage = "Don't have an account?"
    this.switchButtonMessage = "Go to Register";
  }

  clearProfile() {
    this.profileUserData = {
      email: '',
      role: '',
      profileId: 0,
      firstName: '',
      lastName: '',
      joinDate: ''
    }
  }

  submit() {
    if (this.isLogin) {
      this.loginUser();
    } else {
      this.registerUser();
    }
  }

  registerUser() {
    const registerUserData: RegisterUserData = {
      firstName: this.registerFirstName!.value!,
      lastName: this.registerLastName!.value!,
      email: this.registerEmail!.value!,
      password: this.registerPassword!.value!
    }
    this.authService.registerUser(registerUserData).subscribe(
      (response: any) => {
        this.isSignInVisible = false;
        this.messageService.add({severity: 'success', summary: 'Register', detail: response.message});
      }, (error: any) => {
        this.clearSignInFields();
        this.messageService.add({severity: 'error', summary: 'Register error', detail: error.error.errorMessage});
      }
    );
  }

  loginUser() {
    const loginUserData: LoginUserData = {
      email: this.loginEmail!.value!,
      password: this.loginPassword!.value!
    }
    this.authService.loginUser(loginUserData).subscribe(
      (response: any) => {
        localStorage.setItem('token', response.data.token);
        this.isSignInVisible = false;
        this.messageService.add({severity: 'success', summary: 'Login', detail: response.message});
        this.getUserData();
      }, (error: any) => {
        this.clearSignInFields();
        this.messageService.add({severity: 'error', summary: 'Login error', detail: error.error.errorMessage});
      }
    );
  }

  logoutUser() {
    this.authService.logoutUser().subscribe(
      (response: any) => {
        localStorage.removeItem('token');
        this.isProfileVisible = false;
        this.router.navigate(["/search-results/all"]);
        this.messageService.add({severity: 'success', summary: 'Logout', detail: response.message});
        this.clearProfile();
      }
    )
  }

  getUserData() {
    this.userService.getUserData().subscribe(
      (response: any) => {
        this.profileUserData.profileId = response.data.userInfo.profileId;
        this.profileUserData.email = response.data.userInfo.email;
        this.profileUserData.role = response.data.userInfo.role;
        this.profileUserData.firstName = response.data.userInfo.firstName;
        this.profileUserData.lastName = response.data.userInfo.lastName;
        this.profileUserData.joinDate = response.data.userInfo.joinDate;
      }
    );
  }

  search() {
    if (this.searchInput.length !== 0) {
      this.router.navigate(['/search-results', this.searchInput]);
      this.searchInput = '';
    }
  }
}
