import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICourseEnrolment, CourseEnrolment } from 'app/shared/model/course-enrolment.model';
import { CourseEnrolmentService } from './course-enrolment.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';

@Component({
  selector: 'jhi-course-enrolment-update',
  templateUrl: './course-enrolment-update.component.html',
})
export class CourseEnrolmentUpdateComponent implements OnInit {
  isSaving = false;
  students: IStudent[] = [];
  enrolmentDateDp: any;

  editForm = this.fb.group({
    id: [],
    enrolmentDate: [null, [Validators.required]],
    studentId: [],
  });

  constructor(
    protected courseEnrolmentService: CourseEnrolmentService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseEnrolment }) => {
      this.updateForm(courseEnrolment);

      this.studentService
        .query({ filter: 'courseenrolment-is-null' })
        .pipe(
          map((res: HttpResponse<IStudent[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IStudent[]) => {
          if (!courseEnrolment.studentId) {
            this.students = resBody;
          } else {
            this.studentService
              .find(courseEnrolment.studentId)
              .pipe(
                map((subRes: HttpResponse<IStudent>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IStudent[]) => (this.students = concatRes));
          }
        });
    });
  }

  updateForm(courseEnrolment: ICourseEnrolment): void {
    this.editForm.patchValue({
      id: courseEnrolment.id,
      enrolmentDate: courseEnrolment.enrolmentDate,
      studentId: courseEnrolment.studentId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseEnrolment = this.createFromForm();
    if (courseEnrolment.id !== undefined) {
      this.subscribeToSaveResponse(this.courseEnrolmentService.update(courseEnrolment));
    } else {
      this.subscribeToSaveResponse(this.courseEnrolmentService.create(courseEnrolment));
    }
  }

  private createFromForm(): ICourseEnrolment {
    return {
      ...new CourseEnrolment(),
      id: this.editForm.get(['id'])!.value,
      enrolmentDate: this.editForm.get(['enrolmentDate'])!.value,
      studentId: this.editForm.get(['studentId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseEnrolment>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IStudent): any {
    return item.id;
  }
}
