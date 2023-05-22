import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseModule } from 'app/shared/model/course-module.model';
import { CourseModuleService } from './course-module.service';

@Component({
  templateUrl: './course-module-delete-dialog.component.html',
})
export class CourseModuleDeleteDialogComponent {
  courseModule?: ICourseModule;

  constructor(
    protected courseModuleService: CourseModuleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseModuleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('courseModuleListModification');
      this.activeModal.close();
    });
  }
}
