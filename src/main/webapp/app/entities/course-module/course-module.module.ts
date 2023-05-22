import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EduOnlineSharedModule } from 'app/shared/shared.module';
import { CourseModuleComponent } from './course-module.component';
import { CourseModuleDetailComponent } from './course-module-detail.component';
import { CourseModuleUpdateComponent } from './course-module-update.component';
import { CourseModuleDeleteDialogComponent } from './course-module-delete-dialog.component';
import { courseModuleRoute } from './course-module.route';

@NgModule({
  imports: [EduOnlineSharedModule, RouterModule.forChild(courseModuleRoute)],
  declarations: [CourseModuleComponent, CourseModuleDetailComponent, CourseModuleUpdateComponent, CourseModuleDeleteDialogComponent],
  entryComponents: [CourseModuleDeleteDialogComponent],
})
export class EduOnlineCourseModuleModule {}
