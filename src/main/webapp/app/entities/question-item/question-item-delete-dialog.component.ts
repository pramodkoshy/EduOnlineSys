import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IQuestionItem } from 'app/shared/model/question-item.model';
import { QuestionItemService } from './question-item.service';

@Component({
  templateUrl: './question-item-delete-dialog.component.html',
})
export class QuestionItemDeleteDialogComponent {
  questionItem?: IQuestionItem;

  constructor(
    protected questionItemService: QuestionItemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionItemService.delete(id).subscribe(() => {
      this.eventManager.broadcast('questionItemListModification');
      this.activeModal.close();
    });
  }
}
