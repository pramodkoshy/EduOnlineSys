import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EduOnlineSharedModule } from 'app/shared/shared.module';
import { CourseEnrolmentComponent } from './course-enrolment.component';
import { CourseEnrolmentDetailComponent } from './course-enrolment-detail.component';
import { CourseEnrolmentUpdateComponent } from './course-enrolment-update.component';
import { CourseEnrolmentDeleteDialogComponent } from './course-enrolment-delete-dialog.component';
import { courseEnrolmentRoute } from './course-enrolment.route';

@NgModule({
  imports: [EduOnlineSharedModule, RouterModule.forChild(courseEnrolmentRoute)],
  declarations: [
    CourseEnrolmentComponent,
    CourseEnrolmentDetailComponent,
    CourseEnrolmentUpdateComponent,
    CourseEnrolmentDeleteDialogComponent,
  ],
  entryComponents: [CourseEnrolmentDeleteDialogComponent],
})
export class EduOnlineCourseEnrolmentModule {}
