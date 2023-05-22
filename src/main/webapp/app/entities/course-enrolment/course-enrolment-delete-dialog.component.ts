import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseEnrolment } from 'app/shared/model/course-enrolment.model';
import { CourseEnrolmentService } from './course-enrolment.service';

@Component({
  templateUrl: './course-enrolment-delete-dialog.component.html',
})
export class CourseEnrolmentDeleteDialogComponent {
  courseEnrolment?: ICourseEnrolment;

  constructor(
    protected courseEnrolmentService: CourseEnrolmentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseEnrolmentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('courseEnrolmentListModification');
      this.activeModal.close();
    });
  }
}
