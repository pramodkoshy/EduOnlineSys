import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IExamRegistration, ExamRegistration } from 'app/shared/model/exam-registration.model';
import { ExamRegistrationService } from './exam-registration.service';
import { ITeacher } from 'app/shared/model/teacher.model';
import { TeacherService } from 'app/entities/teacher/teacher.service';

@Component({
  selector: 'jhi-exam-registration-update',
  templateUrl: './exam-registration-update.component.html',
})
export class ExamRegistrationUpdateComponent implements OnInit {
  isSaving = false;
  teachers: ITeacher[] = [];
  enrolmentDateDp: any;

  editForm = this.fb.group({
    id: [],
    enrolmentDate: [null, [Validators.required]],
    teacherId: [],
  });

  constructor(
    protected examRegistrationService: ExamRegistrationService,
    protected teacherService: TeacherService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ examRegistration }) => {
      this.updateForm(examRegistration);

      this.teacherService
        .query({ filter: 'examregistration-is-null' })
        .pipe(
          map((res: HttpResponse<ITeacher[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITeacher[]) => {
          if (!examRegistration.teacherId) {
            this.teachers = resBody;
          } else {
            this.teacherService
              .find(examRegistration.teacherId)
              .pipe(
                map((subRes: HttpResponse<ITeacher>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITeacher[]) => (this.teachers = concatRes));
          }
        });
    });
  }

  updateForm(examRegistration: IExamRegistration): void {
    this.editForm.patchValue({
      id: examRegistration.id,
      enrolmentDate: examRegistration.enrolmentDate,
      teacherId: examRegistration.teacherId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const examRegistration = this.createFromForm();
    if (examRegistration.id !== undefined) {
      this.subscribeToSaveResponse(this.examRegistrationService.update(examRegistration));
    } else {
      this.subscribeToSaveResponse(this.examRegistrationService.create(examRegistration));
    }
  }

  private createFromForm(): IExamRegistration {
    return {
      ...new ExamRegistration(),
      id: this.editForm.get(['id'])!.value,
      enrolmentDate: this.editForm.get(['enrolmentDate'])!.value,
      teacherId: this.editForm.get(['teacherId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExamRegistration>>): void {
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

  trackById(index: number, item: ITeacher): any {
    return item.id;
  }
}
