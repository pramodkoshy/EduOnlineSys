import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseUnit } from 'app/shared/model/course-unit.model';
import { CourseUnitService } from './course-unit.service';

@Component({
  templateUrl: './course-unit-delete-dialog.component.html',
})
export class CourseUnitDeleteDialogComponent {
  courseUnit?: ICourseUnit;

  constructor(
    protected courseUnitService: CourseUnitService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseUnitService.delete(id).subscribe(() => {
      this.eventManager.broadcast('courseUnitListModification');
      this.activeModal.close();
    });
  }
}
