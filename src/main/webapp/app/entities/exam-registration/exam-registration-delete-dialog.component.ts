import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExamRegistration } from 'app/shared/model/exam-registration.model';
import { ExamRegistrationService } from './exam-registration.service';

@Component({
  templateUrl: './exam-registration-delete-dialog.component.html',
})
export class ExamRegistrationDeleteDialogComponent {
  examRegistration?: IExamRegistration;

  constructor(
    protected examRegistrationService: ExamRegistrationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.examRegistrationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('examRegistrationListModification');
      this.activeModal.close();
    });
  }
}
