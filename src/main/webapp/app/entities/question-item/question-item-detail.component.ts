import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionItem } from 'app/shared/model/question-item.model';

@Component({
  selector: 'jhi-question-item-detail',
  templateUrl: './question-item-detail.component.html',
})
export class QuestionItemDetailComponent implements OnInit {
  questionItem: IQuestionItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionItem }) => (this.questionItem = questionItem));
  }

  previousState(): void {
    window.history.back();
  }
}
