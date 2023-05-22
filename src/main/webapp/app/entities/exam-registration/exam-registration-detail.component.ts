import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExamRegistration } from 'app/shared/model/exam-registration.model';

@Component({
  selector: 'jhi-exam-registration-detail',
  templateUrl: './exam-registration-detail.component.html',
})
export class ExamRegistrationDetailComponent implements OnInit {
  examRegistration: IExamRegistration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ examRegistration }) => (this.examRegistration = examRegistration));
  }

  previousState(): void {
    window.history.back();
  }
}
