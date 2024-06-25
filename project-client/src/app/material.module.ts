import { NgModule } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { CarouselModule } from 'primeng/carousel';
import { StepsModule } from 'primeng/steps';
import { DialogModule } from 'primeng/dialog';
import { PasswordModule } from 'primeng/password';
import { DividerModule } from 'primeng/divider';
import { SidebarModule } from 'primeng/sidebar';
import { KeyFilterModule } from 'primeng/keyfilter';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { TabViewModule } from 'primeng/tabview';
import { ChipModule } from 'primeng/chip';
import { CheckboxModule } from 'primeng/checkbox';
import { SplitButtonModule } from 'primeng/splitbutton';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { GalleriaModule } from 'primeng/galleria';
import { ToastModule } from 'primeng/toast';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { FileUploadModule } from 'primeng/fileupload';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { AccordionModule } from 'primeng/accordion';
import { ImageModule } from 'primeng/image';
import { FieldsetModule } from 'primeng/fieldset';
import { RatingModule } from 'primeng/rating';
import { TooltipModule } from 'primeng/tooltip';
import { BadgeModule } from 'primeng/badge';
import { DropdownModule } from 'primeng/dropdown';
import { RadioButtonModule } from 'primeng/radiobutton';
import { PaginatorModule } from 'primeng/paginator';

const MaterialComponents = [
  ButtonModule,
  ToolbarModule,
  InputTextModule,
  CardModule,
  CarouselModule,
  StepsModule,
  DialogModule,
  PasswordModule,
  DividerModule,
  SidebarModule,
  KeyFilterModule,
  MessagesModule,
  MessageModule,
  TabViewModule,
  ChipModule,
  CheckboxModule,
  SplitButtonModule,
  ProgressSpinnerModule,
  GalleriaModule,
  ToastModule,
  InputTextareaModule,
  FileUploadModule,
  ConfirmDialogModule,
  AccordionModule,
  ImageModule,
  FieldsetModule,
  RatingModule,
  TooltipModule,
  BadgeModule,
  DropdownModule,
  RadioButtonModule,
  PaginatorModule
];

@NgModule({
  imports: [MaterialComponents],
  exports: [MaterialComponents]
})
export class MaterialModule { }
