import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EduOnlineSharedModule } from 'app/shared/shared.module';
import { CourseUnitComponent } from './course-unit.component';
import { CourseUnitDetailComponent } from './course-unit-detail.component';
import { CourseUnitUpdateComponent } from './course-unit-update.component';
import { CourseUnitDeleteDialogComponent } from './course-unit-delete-dialog.component';
import { courseUnitRoute } from './course-unit.route';

@NgModule({
  imports: [EduOnlineSharedModule, RouterModule.forChild(courseUnitRoute)],
  declarations: [CourseUnitComponent, CourseUnitDetailComponent, CourseUnitUpdateComponent, CourseUnitDeleteDialogComponent],
  entryComponents: [CourseUnitDeleteDialogComponent],
})
export class EduOnlineCourseUnitModule {}
