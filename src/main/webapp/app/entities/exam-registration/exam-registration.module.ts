import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EduOnlineSharedModule } from 'app/shared/shared.module';
import { ExamRegistrationComponent } from './exam-registration.component';
import { ExamRegistrationDetailComponent } from './exam-registration-detail.component';
import { ExamRegistrationUpdateComponent } from './exam-registration-update.component';
import { ExamRegistrationDeleteDialogComponent } from './exam-registration-delete-dialog.component';
import { examRegistrationRoute } from './exam-registration.route';

@NgModule({
  imports: [EduOnlineSharedModule, RouterModule.forChild(examRegistrationRoute)],
  declarations: [
    ExamRegistrationComponent,
    ExamRegistrationDetailComponent,
    ExamRegistrationUpdateComponent,
    ExamRegistrationDeleteDialogComponent,
  ],
  entryComponents: [ExamRegistrationDeleteDialogComponent],
})
export class EduOnlineExamRegistrationModule {}
