import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseLearningObjects } from 'app/shared/model/course-learning-objects.model';
import { CourseLearningObjectsService } from './course-learning-objects.service';

@Component({
  templateUrl: './course-learning-objects-delete-dialog.component.html',
})
export class CourseLearningObjectsDeleteDialogComponent {
  courseLearningObjects?: ICourseLearningObjects;

  constructor(
    protected courseLearningObjectsService: CourseLearningObjectsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseLearningObjectsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('courseLearningObjectsListModification');
      this.activeModal.close();
    });
  }
}
