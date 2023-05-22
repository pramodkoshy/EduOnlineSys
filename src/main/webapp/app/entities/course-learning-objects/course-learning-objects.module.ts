import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EduOnlineSharedModule } from 'app/shared/shared.module';
import { CourseLearningObjectsComponent } from './course-learning-objects.component';
import { CourseLearningObjectsDetailComponent } from './course-learning-objects-detail.component';
import { CourseLearningObjectsUpdateComponent } from './course-learning-objects-update.component';
import { CourseLearningObjectsDeleteDialogComponent } from './course-learning-objects-delete-dialog.component';
import { courseLearningObjectsRoute } from './course-learning-objects.route';

@NgModule({
  imports: [EduOnlineSharedModule, RouterModule.forChild(courseLearningObjectsRoute)],
  declarations: [
    CourseLearningObjectsComponent,
    CourseLearningObjectsDetailComponent,
    CourseLearningObjectsUpdateComponent,
    CourseLearningObjectsDeleteDialogComponent,
  ],
  entryComponents: [CourseLearningObjectsDeleteDialogComponent],
})
export class EduOnlineCourseLearningObjectsModule {}
