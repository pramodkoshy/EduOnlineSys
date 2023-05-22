import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExamRegistration, ExamRegistration } from 'app/shared/model/exam-registration.model';
import { ExamRegistrationService } from './exam-registration.service';
import { ExamRegistrationComponent } from './exam-registration.component';
import { ExamRegistrationDetailComponent } from './exam-registration-detail.component';
import { ExamRegistrationUpdateComponent } from './exam-registration-update.component';

@Injectable({ providedIn: 'root' })
export class ExamRegistrationResolve implements Resolve<IExamRegistration> {
  constructor(private service: ExamRegistrationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExamRegistration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((examRegistration: HttpResponse<ExamRegistration>) => {
          if (examRegistration.body) {
            return of(examRegistration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExamRegistration());
  }
}

export const examRegistrationRoute: Routes = [
  {
    path: '',
    component: ExamRegistrationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'eduOnlineApp.examRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExamRegistrationDetailComponent,
    resolve: {
      examRegistration: ExamRegistrationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.examRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExamRegistrationUpdateComponent,
    resolve: {
      examRegistration: ExamRegistrationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.examRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExamRegistrationUpdateComponent,
    resolve: {
      examRegistration: ExamRegistrationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.examRegistration.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
