import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAnswerItem, AnswerItem } from 'app/shared/model/answer-item.model';
import { AnswerItemService } from './answer-item.service';

@Component({
  selector: 'jhi-answer-item-update',
  templateUrl: './answer-item-update.component.html',
})
export class AnswerItemUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]],
  });

  constructor(protected answerItemService: AnswerItemService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answerItem }) => {
      this.updateForm(answerItem);
    });
  }

  updateForm(answerItem: IAnswerItem): void {
    this.editForm.patchValue({
      id: answerItem.id,
      number: answerItem.number,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const answerItem = this.createFromForm();
    if (answerItem.id !== undefined) {
      this.subscribeToSaveResponse(this.answerItemService.update(answerItem));
    } else {
      this.subscribeToSaveResponse(this.answerItemService.create(answerItem));
    }
  }

  private createFromForm(): IAnswerItem {
    return {
      ...new AnswerItem(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswerItem>>): void {
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
}
