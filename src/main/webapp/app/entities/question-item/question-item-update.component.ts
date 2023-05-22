import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IQuestionItem, QuestionItem } from 'app/shared/model/question-item.model';
import { QuestionItemService } from './question-item.service';
import { IAnswerItem } from 'app/shared/model/answer-item.model';
import { AnswerItemService } from 'app/entities/answer-item/answer-item.service';
import { IQuestion } from 'app/shared/model/question.model';
import { QuestionService } from 'app/entities/question/question.service';

type SelectableEntity = IAnswerItem | IQuestion;

@Component({
  selector: 'jhi-question-item-update',
  templateUrl: './question-item-update.component.html',
})
export class QuestionItemUpdateComponent implements OnInit {
  isSaving = false;
  answeritems: IAnswerItem[] = [];
  questions: IQuestion[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    number: [null, [Validators.required]],
    answerItemId: [],
    questionId: [],
  });

  constructor(
    protected questionItemService: QuestionItemService,
    protected answerItemService: AnswerItemService,
    protected questionService: QuestionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionItem }) => {
      this.updateForm(questionItem);

      this.answerItemService
        .query({ filter: 'questionitem-is-null' })
        .pipe(
          map((res: HttpResponse<IAnswerItem[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IAnswerItem[]) => {
          if (!questionItem.answerItemId) {
            this.answeritems = resBody;
          } else {
            this.answerItemService
              .find(questionItem.answerItemId)
              .pipe(
                map((subRes: HttpResponse<IAnswerItem>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAnswerItem[]) => (this.answeritems = concatRes));
          }
        });

      this.questionService.query().subscribe((res: HttpResponse<IQuestion[]>) => (this.questions = res.body || []));
    });
  }

  updateForm(questionItem: IQuestionItem): void {
    this.editForm.patchValue({
      id: questionItem.id,
      name: questionItem.name,
      number: questionItem.number,
      answerItemId: questionItem.answerItemId,
      questionId: questionItem.questionId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionItem = this.createFromForm();
    if (questionItem.id !== undefined) {
      this.subscribeToSaveResponse(this.questionItemService.update(questionItem));
    } else {
      this.subscribeToSaveResponse(this.questionItemService.create(questionItem));
    }
  }

  private createFromForm(): IQuestionItem {
    return {
      ...new QuestionItem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      number: this.editForm.get(['number'])!.value,
      answerItemId: this.editForm.get(['answerItemId'])!.value,
      questionId: this.editForm.get(['questionId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionItem>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
