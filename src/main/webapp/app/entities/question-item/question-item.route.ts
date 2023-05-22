import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IQuestionItem, QuestionItem } from 'app/shared/model/question-item.model';
import { QuestionItemService } from './question-item.service';
import { QuestionItemComponent } from './question-item.component';
import { QuestionItemDetailComponent } from './question-item-detail.component';
import { QuestionItemUpdateComponent } from './question-item-update.component';

@Injectable({ providedIn: 'root' })
export class QuestionItemResolve implements Resolve<IQuestionItem> {
  constructor(private service: QuestionItemService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((questionItem: HttpResponse<QuestionItem>) => {
          if (questionItem.body) {
            return of(questionItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuestionItem());
  }
}

export const questionItemRoute: Routes = [
  {
    path: '',
    component: QuestionItemComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'eduOnlineApp.questionItem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionItemDetailComponent,
    resolve: {
      questionItem: QuestionItemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.questionItem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionItemUpdateComponent,
    resolve: {
      questionItem: QuestionItemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.questionItem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionItemUpdateComponent,
    resolve: {
      questionItem: QuestionItemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.questionItem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
