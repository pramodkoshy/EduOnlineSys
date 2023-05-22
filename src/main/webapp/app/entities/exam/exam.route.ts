import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExam, Exam } from 'app/shared/model/exam.model';
import { ExamService } from './exam.service';
import { ExamComponent } from './exam.component';
import { ExamDetailComponent } from './exam-detail.component';
import { ExamUpdateComponent } from './exam-update.component';

@Injectable({ providedIn: 'root' })
export class ExamResolve implements Resolve<IExam> {
  constructor(private service: ExamService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExam> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((exam: HttpResponse<Exam>) => {
          if (exam.body) {
            return of(exam.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Exam());
  }
}

export const examRoute: Routes = [
  {
    path: '',
    component: ExamComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'eduOnlineApp.exam.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExamDetailComponent,
    resolve: {
      exam: ExamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.exam.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExamUpdateComponent,
    resolve: {
      exam: ExamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.exam.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExamUpdateComponent,
    resolve: {
      exam: ExamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.exam.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
