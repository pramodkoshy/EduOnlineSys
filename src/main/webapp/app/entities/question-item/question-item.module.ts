import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EduOnlineSharedModule } from 'app/shared/shared.module';
import { QuestionItemComponent } from './question-item.component';
import { QuestionItemDetailComponent } from './question-item-detail.component';
import { QuestionItemUpdateComponent } from './question-item-update.component';
import { QuestionItemDeleteDialogComponent } from './question-item-delete-dialog.component';
import { questionItemRoute } from './question-item.route';

@NgModule({
  imports: [EduOnlineSharedModule, RouterModule.forChild(questionItemRoute)],
  declarations: [QuestionItemComponent, QuestionItemDetailComponent, QuestionItemUpdateComponent, QuestionItemDeleteDialogComponent],
  entryComponents: [QuestionItemDeleteDialogComponent],
})
export class EduOnlineQuestionItemModule {}
