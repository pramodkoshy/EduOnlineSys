import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EduOnlineSharedModule } from 'app/shared/shared.module';
import { AnswerItemComponent } from './answer-item.component';
import { AnswerItemDetailComponent } from './answer-item-detail.component';
import { AnswerItemUpdateComponent } from './answer-item-update.component';
import { AnswerItemDeleteDialogComponent } from './answer-item-delete-dialog.component';
import { answerItemRoute } from './answer-item.route';

@NgModule({
  imports: [EduOnlineSharedModule, RouterModule.forChild(answerItemRoute)],
  declarations: [AnswerItemComponent, AnswerItemDetailComponent, AnswerItemUpdateComponent, AnswerItemDeleteDialogComponent],
  entryComponents: [AnswerItemDeleteDialogComponent],
})
export class EduOnlineAnswerItemModule {}
