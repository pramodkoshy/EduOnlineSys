import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnswerItem } from 'app/shared/model/answer-item.model';
import { AnswerItemService } from './answer-item.service';

@Component({
  templateUrl: './answer-item-delete-dialog.component.html',
})
export class AnswerItemDeleteDialogComponent {
  answerItem?: IAnswerItem;

  constructor(
    protected answerItemService: AnswerItemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.answerItemService.delete(id).subscribe(() => {
      this.eventManager.broadcast('answerItemListModification');
      this.activeModal.close();
    });
  }
}
